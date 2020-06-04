# HOME DOC

This is a library to provide Home Document of an application/microservice.

## Configuration

Home document works fine without any configuration.
There is a set of properties which can be used change home document attributes:
 
    home-doc:
      home-path: /home # Path of url under which home document is available. Default id "/home"
      self-rel:  self  # Name of self link in home document. Default is "self" 
      rels:            # Map of replacements for rels. Default is empty map e.i. rels are defined by @HomeDoc annotation              
        url-to-replace: url-replacement
 

## Versioning

### 1.0.0

- Initial version

## Exposed services

- HomeDocController

## Configuration

Location of HomeDoc endpoint. Default is /home

    home-doc.home-path=/v1/home

## Usage

1. Add dependency to an application/microservice

        <dependency>
            <groupId>pl.jacekniezgoda</groupId>
            <artifactId>home-doc</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
 
1. Add @HomeDoc annotation to controller methods (e.i. methods annotated with RequestMapping). E.g.
 
   - HomeDoc with key selected from method name e.i. "find" in this case
   
        @GetMapping("/{id}")
        @HomeDoc
        public Object find() {...};

   - HomeDoc with key specified explicit

        @GetMapping("/{id}")
        @HomeDoc("find"")

   - HomeDoc with type specified by produces attribute of @RequestMapping annotation

        @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        @HomeDoc

   - HomeDoc with template specified

        @GetMapping("/{id}")
        @HomeDoc(template = "/{id}")

   - HomeDoc with describedBy specified

        @PutMapping()
        @HomeDoc(describedBy = "/v1/definitions/update")
        
   - HomeDoc with scopes specified explicit

        @PutMapping()
        @HomeDoc(authorities = {"ROLE_ADMIN", "ROLE_USER"})
        
   - HomeDoc with scopes specified by @Secured annotation

        @PutMapping()
        @HomeDoc
        @Secured({"ROLE_ADMIN", "ROLE_USER"})

   - HomeDoc with authorities specified by @RolesAllowed annotation

        @PutMapping()
        @HomeDoc
        @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})

1. Run application (micro service)

1. Call home endpoint.
 
        http://localhost:8080/v1/home
        
1. That's all Folks.