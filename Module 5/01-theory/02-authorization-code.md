# Authorization Code

Learn how an authorization code grant can be used for getting an access token.

> We'll cover the following:
>
> - Step One
> - Step Two

The authorization code grant is used by confidential and public clients to exchange an authorization code for an access token.

## Step one

The client redirects the user to the authorization server appending the following parameters as a query string:

- response type
- client_id
- redirect_uri
- scope
- state

        http://authserveraddress/?response_type=code&client_id=<client identifier>&redirect_uri=<the url to redirect to>&scope=<space delimited list of scopes>&state=<state parameter>

These parameteres will be checked by the authorization server.

At this point, the user will be prompted to enter his login credentials.

If the user completes the login form successfully, the client will be redirected from the authorization server to the client (to the redirect URI) with the following parameters in the query string:

- code
- state

        http://redirecturi/?code=<auth code>&state=<state param>

## Step two

The client sends a POST request to the authorization server with these parameters:

- grant_type (contains the authorization code)
- client_id (contains the cleint identifier)
- client_secret (contains the client secret)
- redirect_uri (contains the same redirect URI the user was redirect back to)
- code (represents the authorization code from the query string)

The authorization server answers with a JSON object that contains these properties:

- token_type, which is usually "Bearer"
- expires_in represents the amount of time after which the token will expire
- access_token the access token itself
- refresh_token is used to gain a new access token when the previous one expires

        {
        "token_type": "Bearer",
        "expires_in": "3600",
        "access_token": "the access token",
        "refresh_token": "the refresh token"
        }
