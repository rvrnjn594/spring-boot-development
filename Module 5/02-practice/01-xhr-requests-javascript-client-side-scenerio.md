# XHR Requests (JavaScript client-side scenario)

Learn how to set JavaScript for the client-side in an agile way.

> We'll cover the following:
>
> - CORS policies
> - Send login credentials via JSON (/oauth/token) - Authorization Server
> - Important note

You need to set a copule of things before you can use JavaScript for the client-side in an agile way.

## CORS policies

If you want to use JavaScript and XML HTTP Request (XHR) for the client-side, you will need to create a filter on your authorization server that sets the Cross-Origin Resource Sharing (CORS) policies for every request.

How you do this will differ depending on the Spring version you use.

For Spring Boot 1.5, you might need to create a custom bean for the WebMvcConfigure class like so:

        @Configuration
        public class WebConfig {
            @Bean
            public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                    @Override
                    public void addCorsMappings(CorsRegistry registry) {
                        registry.addMapping("/oauth/**");
                    }
                };
            }
        }

However, for Spring Boot 2.1.x we need to use better artillery.

We are going to create a bean that extends **OncePerRequestFilter:**

        @Component
        @Order(PriorityOrdered.HIGHEST_PRECEDENCE)
        public class CorsFilter extends OncePerRequestFilter {
                @Override
                @SneakyThrows
                protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
                    if (req.getServletPath().equals("/oauth/token")) {
                        res.addHeader("Access-Control-Allow-Origin", "*");
                        res.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                        res.addHeader("Access-Control-Max-Age", "3600");
                        res.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
                        if (req.getMethod().equals(HttpMethod.OPTIONS.name())) {
                            res.setStatus(HttpServletResponse.SC_OK);
                        } else {
                            chain.doFilter(req, res);
                        }
                    } else {
                        chain.doFilter(req, res);
                    }
                }
        }

## Send login credentials via JSON (/oauth/token) - Authorization Server

This scenerio is what we typically have when we send an AJAX call where a JSON object is sent and is particularly useful in the password grant scenerio.

In the doFilter method of JsonToUrlEncodedAuthenticationFilter, we rely on the private request field to convert the JSON object sent from our client. We use reflection and set the field as accessible. The request field at this stage should contain data in the form of a hashmap instead of JSON data.

The reason behind this is that the Oauth2 original standard requires us to use application/x-www-form-urlencoded, and Spring Security does not allow other possibilities contrary to Postman, for example. At this stage, the URL-encoded data in the request is expected to be in the form of a HashMap. Now we need to convert the data from JSON (as sent by our Javascript client) to HashMap.

        @Override
        @SneakyThrows
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
            Field f = request.getClass().getDeclaredField("request");
            f.setAccessible(true);
            Request realRequest = (Request) f.get(request);

            //Request content type without spaces (inner spaces matter)
            //trim deletes spaces only at the beginning and at the end of the string
            String contentType = realRequest.getContentType().toLowerCase().chars()
                    .mapToObj(c -> String.valueOf((char) c))
                    .filter(x->!x.equals(" "))
                    .collect(Collectors.joining());

            if ((contentType.equals(MediaType.APPLICATION_JSON_UTF8_VALUE.toLowerCase())||
                    contentType.equals(MediaType.APPLICATION_JSON_VALUE.toLowerCase()))
                            && Objects.equals((realRequest).getServletPath(), "/oauth/token")) {

                InputStream is = realRequest.getInputStream();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is), 16384)) {
                    String json = br.lines()
                            .collect(Collectors.joining(System.lineSeparator()));
                    HashMap<String, String> result = mapper.readValue(json, HashMap.class);
                    HashMap<String, String[]> r = new HashMap<>();

                    for (String key : result.keySet()) {
                        String[] val = new String[1];
                        val[0] = result.get(key);
                        r.put(key, val);
                    }
                    String[] val = new String[1];
                    val[0] = (realRequest).getMethod();
                    r.put("_method", val);

                    HttpServletRequest s = new MyServletRequestWrapper(((HttpServletRequest) request), r);
                    chain.doFilter(s, response);
                }

            } else {
                chain.doFilter(request, response);
            }
        }

All of those ceremonies allow us to send some data inside the body field of our request object with application/json content type instead of application/x-www-form-urlencoded.

This implies that this data can be sent more conveniently using JavaScript as the HTTP client of choice. For example, fetch can be used in a straightforward manner, meaning hidden form inputs to hold the contents of my object won’t have to be used.

![example of Oauth2 Password with Postman](./1-1-example-of-oauth2-password-grant-with-postman.png)

## Important Note

To avoid confusion, please note that, unless expressed specifically, the POST requests in the aforementioned flows are not expected to send JSON objects by default.

To send a JSON object, you need to use something like the above filter for the server to understand.
