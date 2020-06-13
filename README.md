# Real Estate Management System

A backend service prototype for real managing real estate properties and requirements

## Features
#### Property
* ##### Upload 
    ###### Request:
    Clients can upload their property using the api:
    ```
    [POST] http://localhost:9090/property/    
    ``` 
    With request body (all fields mandatory):
    ```
    {
    	"name": <String>,
    	"latitude": <Float>,
    	"longitude": <Float>,
    	"price": <Float>,
    	"bedroom_count": <Float>,   // to support 2.5, 3.5 BHKs (Weird, right?)
    	"bathroom_count": <Integer>
    }
    ```
    ###### Validations:
    * Basic validations on each field
    * Bedroom count can only be multiples of 0.5, i.e., 1, 2, 2.5, 3.5, etc. 
    * An already existing property cannot be uploaded again.  
    If done so, an error message with existing properties' id is given as response.
    
    ###### Response:
    ```
    201
    {
        "property_id": <Integer>,
        "requirements": [
            {
                "id": <Integer>,
                "latitude": <Float>,
                "longitude": <Float>,
                "min_budget": <Float>,
                "max_budget": <Float>,
                "min_bedrooms": <Float>,
                "max_bedrooms": <Float>,
                "min_bathrooms": <Integer>,
                "max_bathrooms": <Integer>
            }, ...
        ],
        "offset": <Integer>,
        "total_count": <Integer>
    }
    ```
    ###### Note:
    In the response, 'requirements' are paginated. Max 10 requirements can be served.  
    Property's matching requirements search api can be used to fetch the next set of requirements.
    
* ##### Search matching requirements
    ###### Request:
    Clients can search matching requirements for their uploaded property using the api and uploaded property's id:
    ```
    [GET] http://localhost:9090/property/<property_id>/requirements?offset=<Integer>&page_size=<Integer>
    ``` 
    Default offset: 0 and default page size: 10
    ###### Response:
        ```
        200
        {
            "requirements": [
                {
                    "id": <Integer>,
                    "latitude": <Float>,
                    "longitude": <Float>,
                    "min_budget": <Float>,
                    "max_budget": <Float>,
                    "min_bedrooms": <Float>,
                    "max_bedrooms": <Float>,
                    "min_bathrooms": <Integer>,
                    "max_bathrooms": <Integer>
                }, ...
            ],
            "offset": <Integer>,
            "page_size": <Integer>,
            "total_count": <Integer>
        }
        ```

#### Requirement 
* ##### Upload    
    ###### Request:
    Clients can upload their requirement using the api:
    ```
    [POST] http://localhost:9090/requirement/    
    ``` 
    With request body (all fields mandatory):
    ```
    {
    	"latitude": <Float>,
    	"longitude": <Float>,
    	"min_budget": <Float>,
    	"max_budget": <Float>,
    	"min_bedrooms": <Float>,
    	"max_bedrooms": <Float>,
    	"min_bathrooms": <Integer>,
    	"max_bathrooms": <Integer>
    }
    ```
    ###### Validations:
    * Basic validations on each field
    * Min & max bedrooms can only be multiples of 0.5, i.e., 1, 2, 2.5, 3.5, etc.
    * Any one of min/max values is mandatory, i.e., client can send either min budget or max budget, or both
    
    ###### Response:
    ```
    201
    {
        "requirement_id": <Integer>,
        "properties": [
            {
                "id": <Integer>,
                "name": <String>,
                "latitude": <Float>,
                "longitude": <Float>,
                "price": <Float>,
                "bedroom_count": <Float>,
                "bathroom_count": <Integer>
            }, ...
        ],
        "offset": <Integer>,
        "page_size": <Integer>,
        "total_count": <Integer>
    }
    ```
    ###### Note:
    In the response, 'properties' are paginated. By default, 10 properties can be served.  
    Requirement's matching properties search api can be used to fetch the next set of properties.
* ##### Search matching properties
    ###### Request:
    Clients can search matching properties for their uploaded requirement using the api and uploaded requirement's id:
    ```
    [GET] http://localhost:9090/requirement/<requirement_id>/properties?offset=<Integer>&page_size=<Integer>
    ``` 
    Default offset: 0 and default page size: 10
    ###### Response:
        ```
        200
        {
            "properties": [
                {
                    "id": <Integer>,
                    "name": <String>,
                    "latitude": <Float>,
                    "longitude": <Float>,
                    "price": <Float>,
                    "bedroom_count": <Float>,
                    "bathroom_count": <Integer>
                }, ...
            ],
            "offset": <Integer>,
            "page_size": <Integer>,
            "total_count": <Integer>
        }
        ```
        
## Matching Rules
Out of 100% match, the division of weightage is as follows:
* distance: 30%
* budget: 30%
* bedroom: 20%
* bathroom: 20%

A successful match is one which has a total match of more than 40%

Requirements for distance match:
* distance should me within 10 miles
* distance < 2 miles, match: full 30%
* 2 miles < distance < 4 miles, distance match: 24%
* 4 miles < distance < 6 miles, distance match: 18%
* 6 miles < distance < 8 miles, distance match: 12%
* 8 miles < distance < 10 miles, distance match: 6%

Requirements for budget match:
* if only one of min or max is provided, min budget = 0.9 * budget and max budget = 1.1 * budget
* if both min and max both are provided
    * min budget < property price < max budget, budget match: full 30%
    * approx. min budget * 0.94 < property price < max budget * 1.06, budget match: 24%  
    * approx. min budget * 0.88 < property price < max budget * 1.12, budget match: 18%  
    * approx. min budget * 0.81 < property price < max budget * 1.19, budget match: 12%  
    * approx. min budget * 0.75 < property price < max budget * 1.25, budget match: 6%  
    * else, budget match: 0%

Requirements for bedroom/bathroom match:
* if only one of min or max is provided, min number of bathroom/bedroom = max number of bathroom/bedroom
* if min and max both are provided
    * min < property bedroom/bathroom count < max, bedroom/bathroom match: full 20%
    * min - 1 < property bedroom/bathroom count < max + 1, bedroom/bathroom match: 13.33%  
    * min - 2 < property bedroom/bathroom count < max + 2, bedroom/bathroom match: 6.67%  
    * else, bedroom/bathroom match: 0%
 


## Built with
* Spring Boot
* MySQL

## Note
This is a prototype. Upgrades needed:
* Use MySQL's spatial extensions to store laitude & longitude and to calculate distances.  

Suggestions and tips will be highly appreciated.
