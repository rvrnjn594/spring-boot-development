# @PatchMapping

Learn how to perform partial updates to a record in the database.

> We'll cover the following:
>
> - Partial update
>
> 1. Defining the patch() method
> 2. Using reflection
> 3. @PatchMapping
> 4. Queries for partial update

## Partial update

The PUT method updates the whole record. There may be a scenerio when only one or two fields needs to be updated. In that case, sending the whole record does not make sense.  
 The HTTP PATCH method is used for partial updates.

Sometime we may need to update a single field.  
 For example, once we enter a player in our database, the field that will most likely change is his titles count.  
 The player entity only has a few fields and PUT can be used for update.  
 But if the entity is large and contains nested objects, it will have a performance impact to send the whole entity only to update a single field.

So, in our example, partial request means that we only send the titles in the request body instead of the whole Player object.  
 If we use PUT to send a partial request, all other fields are set to null. The code widget below illustrates the point of a PUT request with the following request body is sent to /players/1.

        {
            "title" : 161
        }

We get the following response:

        {
            "id" : 1,
            "name" : null,
            "nationality" : null,
            "birthDate" : null,
            "titles" : 161
        }

The titles have been modified but the rest of the values are now null which is not desired outcome.  
The PUT method requires the entire object to be sent, even when we want to modify a single field. If partial data is sent, then all other fields are set to null.  
PATCH comes in handy in such situations. It allows a list of changes to be applied to the entity, as we will see in this lesson.

![PATCH request to /players/1](./images/9-2-PATCH-request-to-players.png)

## 1. Defining the patch() method

In the PlayerService class, we will implement a method to handle partial updates to the Player object.  
 This method patch() will have two arguments.  
 The first is the id of the player on which the patch is to be applied.  
 The second argument is the Map containing the key-value pairs of the fields that will be updated.  
 The key (field name) is a String while the value is an Object as it can have different datatypes.

        public Player patch(int id, Map<String, Object> playerPatch) {

        }

Inside the method, we will use the id to fetch the existing Player object from the database using the findById() method of the JpaRepository.  
 This method loads the entity from the database unlike the getOne() method, which returns a proxy object without hitting the database.  
 The findById() returns an Optional and we need to check if a Player object is returned using the isPresent() method.

        public Player patch(int id, Map<String, Object> player Patch) {
            Optional<Player> player = repo.findById(id);
            if (player.isPresent()) {
                // update fields using Map
            }
            return repo.save(player);
        }

## 2. Using reflection

Next, we will loop through the Map, find the field that will be updated, and then change the value of that field in the existing Player object that we retrieved from the database in the previous step.  
 The Reflection API is used to examine and modify fields, methods, and classes at runtime.  
 It allows access to private fields of a class and can be used to access the fields irrespective of their access modifiers.

Spring provides the **ReflectionUtils class for handling reflection and working with the Reflection API.**

- The ReflectionUtils class has a findField method to identify the field of an object using a String name. The findField() method takes two arguments, the class having the field and the name of the field in our case is contained in the variable key.  
   This method will return a Field object.

        Field field = ReflectionUtil.findField(Player.class, key);

- To set a value for this field, we need to set the field's accessible flag to true.  
  ReflectionUtils setAccessible() method, when called on a field, toggles its accessible flag.  
  We can also use another method called makeAccessible(). This method makes the given field accessible by calling the setAccessible(true) method if necessary.

        ReflectionUtils.makeAccessible(field);

- Lastly, we will call the setField method and use the value from the Map to set the field in the player object. The setField() method takes three arguments, the reference of the field, the object in which the field is to be set, and the value to set.  
  This method requires that the given field is accessible.

        ReflectionUtil.setField(field, player.get(), value);

  Here, we have used the get() method on the Optional player object to retrieve it.

In this way using reflection, a field can be updated in an object. Since we are passing the fields to be updated as a Map, we will use the above steps while iterating through the map of key-value pairs as follows:

        playerPatch.forEach( (key, value) -> {
            Field field = ReflectinoUtils.findField(Player.class, key);
            ReflectionUtils.makeAccessible(field);
            ReflectionUtil.setField(field, player.get(), value);
        });

The code will iterate through the Map and make desired changes in the player object.  
At the end we will call the save() method to update the player record. The complete code of the method is shown below:

        public Player patch(int id, Map<String, Object> playerPatch) {
            Optional<Player> player = repo.findById(id);

            if (player.isPresent()) {
                // above loop method
            }

            return repo.save(player.get());
        }

Now, we will write a controller method called partialUpdate() to handle PATCH mapping.  
It will have an @PatchMapping annotation with endpoint /players/{id}, where id is a path variable. The method takes a Map argument containing the key-value pair of the fields we want to update.  
Since the field names are String and the values can be any datatype, we will use Map<String, Object>. The list of fields and their values will come in the rquest body and the @RequestBody annotation binds the JSON to the Map variable.

        @PatchMapping("/players/{id}")
        public Player partialUpdate(@PatchVariable int id, @RequestBody Map<String, Object> playerPatch) {
            // call service layer method for patch
        }

## @PatchMapping

The @PatchMapping is a shortcut annotation. It is the same as:

        @RequestMapping(method = RequestMethod.PATCH);

Inside the method, we will simply delegate the call to the service method and pass the player id and the Map with the fields to be updated, along with their values.

        @PatchMapping("/players/{id}")
        public Player partialUpdate(@PathVariable int id. @RequestBody Map<String, Object> playerPatch) {
            return service.patch(id, playerPatch);
        }

To test, use Postman to send a PATCH request to /players/1. For sending JSON data in request body, choose "Body", "raw" and select "JSON" as type. The request body will look like this:

        {
            "titles": 161
        }

A GET request to /players can be sent to verify that the patch has been applied or by checking the H2 database.

## Queries for partial update

A point to note here is that in step 2, we have used to save() method to applying the patch.  
This method updates all the columns in the table.  
For large objects with a lot of fields, this can have a performance impact. To avoid this, we can implement queries for partial updates. These queries can target frequently updated columns.  
 If we want to update the titles column of the Player table, we can create an updateTitles() method implementing a query in the PlayerRepository.  
 This method takes two arguments, the id of the player and the title count.

The @Query annotation is used to implement an update query as follows:

        @Modifying
        @Query("update Player p set p.titles = :titles where p.id = :id")
        void updateTitles(@Param("id") int id, @Param("titles") int titles);

The query mus be used with @Modifying annotation to execute the UPDATE query. The @Param annotation binds the method parameters to the query. This method will only change a single column of the table unlike the save() method which updates all the columms of the table.

After writing the repository method, we will move to the service layer. The service layer will implement the updateTitles() method as follows:

        @Transactional
        public void updateTitles(int id, int titles) {
            repo.updateTitles(id, titles);
        }

The @Transactional annotation ensures that the databse is left in a consistent state. The transaction is either committed or rolled back in case of failure.

Now, in the PlayerController class, we can define a new PATCH mapping for /players/{id}/titles as follows:

        @PatchMapping("/players'{id}/titles")
        public void updateTitles(@PathVaraible int id, @RequestBody int titles) {
            service.updateTitles(id, title);
        }

id is the path variable. This method accepts an integer from the request body. To test the new endpoint, we can send a PATCH request to /players/1/titles.  
 The request body contains an integer value of the title count:

        157

The REST API responds with 200 status code which indicates that the request was successful.

**PlayerController.java**

        package io.datajek.springrest;

        import java.util.List;
        import java.util.Map;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PatchMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RestController;

        @RestController
        public class PlayerController {

            @Autowired
            PlayerService service;

            @GetMapping("/players")
            public List<Player> allPlayers() {
                return service.getAllPlayers();
            }

            @GetMapping("/players/{id}")
            public Player getPlayer(@PathVariable int id){
                return service.getPlayer(id);
            }

            @PatchMapping("/players/{id}")
            public Player partialUpdate(@PathVariable int id, @RequestBody Map<String, Object> playerPatch) {
                return service.patch(id, playerPatch);
            }

            @PatchMapping("/players/{id}/titles")
            public void updateTitles(@PathVariable int id, @RequestBody int titles) {
                service.updateTitles(id, titles);
            }
        }

**PlayerService.java**

        package io.datajek.springrest;

        import java.lang.reflect.Field;
        import java.util.List;
        import java.util.Map;
        import java.util.Optional;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.util.ReflectionUtils;

        @Service
        public class PlayerService {

            @Autowired
            PlayerRepository repo;

            //Get all players
            public List<Player> getAllPlayers() {
                return repo.findAll();
            }

            //Get player by ID
            public Player getPlayer(int id) {
                Optional<Player> tempPlayer = repo.findById(id);

                if(tempPlayer.isEmpty())
                    throw new RuntimeException("Player with id {"+ id +"} not found");

                return tempPlayer.get();
            }

            //Partial update
            public Player patch( int id, Map<String, Object> partialPlayer) {

                Optional<Player> player = repo.findById(id);

                if(player.isPresent()) {
                    partialPlayer.forEach( (key, value) -> {
                        System.out.println("Key: " + key + " Value: " + value);
                        Field field = ReflectionUtils.findField(Player.class, key);
                        ReflectionUtils.makeAccessible(field);
                        ReflectionUtils.setField(field, player.get(), value);
                    });
                }
                else
                    throw new RuntimeException("Player with id {"+ id +"} not found");

                return repo.save(player.get());
            }

            //update a single field
            @Transactional
            public void updateTitles(int id, int titles) {
                Optional<Player> tempPlayer = repo.findById(id);

                if(tempPlayer.isEmpty())
                    throw new RuntimeException("Player with id {"+ id +"} not found");

                repo.updateTitles(id, titles);
            }
        }

**PlayerRepository.java**

        package io.datajek.springrest;

        import java.util.List;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;

        public interface PlayerRepository extends JpaRepository<Player, Integer>{

            @Modifying
            @Query("update Player p set p.titles = :titles where p.id = :id")
            void updateTitles(@Param("id") int id, @Param("titles") int titles);
        }

**Player.java**

        package io.datajek.springrest;

        import java.util.Date;

        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.GenerationType;
        import javax.persistence.Id;

        import com.fasterxml.jackson.annotation.JsonFormat;

        @Entity
        public class Player {
            @Id
            @GeneratedValue(strategy=GenerationType.IDENTITY)
            private int id;
            private String name;
            private String nationality;
            @JsonFormat(pattern = "dd-MM-yyyy")
            private Date birthDate;
            private int titles;

            public Player() {

            }

            public Player(String name, String nationality, Date birthDate, int titles) {
                super();
                this.name = name;
                this.nationality = nationality;
                this.birthDate = birthDate;
                this.titles = titles;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNationality() {
                return nationality;
            }

            public void setNationality(String nationality) {
                this.nationality = nationality;
            }

            public Date getBirthDate() {
                return birthDate;
            }

            public void setBirthDate(Date birthDate) {
                this.birthDate = birthDate;
            }

            public int getTitles() {
                return titles;
            }

            public void setTitles(int titles) {
                this.titles = titles;
            }

            @Override
            public String toString() {
                return "\nPlayer [id= " + id + ", name= " + name + ", nationality= " + nationality + ", birthDate= " + birthDate
                        + ", titles= " + titles + "]";
            }
        }

**TennisPlayerSpringRestApplication.java**

        package io.datajek.springrest;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

        @SpringBootApplication
        public class TennisPlayerSpringRestApplication{


            public static void main(String[] args) {
                SpringApplication.run(TennisPlayerSpringRestApplication.class, args);
            }
        }
