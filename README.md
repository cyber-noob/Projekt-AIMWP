# SAHybridFramework
![appium version](https://img.shields.io/badge/appium-2.0.1-brightgreen)
![appium:java-client version](https://img.shields.io/badge/appium_java_client-2.0.5-brightgreen)
![selenium version](https://img.shields.io/badge/selenium-4.10.0-brightgreen)

A hybrid framework that supports  

:superhero: Android  
:superhero: IOS  
:superhero: WEB  
:superhero: MSITE  
:superhero: PWA  

ui automation in a single repo.

If you have an application that shares more than 80% of business logic flows along 
with elements across multiple platforms, this repo design might be the ideal solution 
for you.

**Why go for a single repository?**  
Less code == Less maintenance

Some the problem statements that were tackled over the repo design:
* Adding **driver capabilities** during driver creation doesn't have any compile time checks 
that might lead to some gnarly debugging sessions.
* No compile time checks for **Mandatory Properties** might lead to unexpected 
automation runs or inability to run automation framework in general causing 
downtimes to get started.
* How do we **gracefully handle multiple platforms** without code clutters?
* How to make the **locators more readable** in terms of its functionality in UI and reduce duplication?

If all these questions interests you, then you are at the right place. Lets start with our solution.

### Appium Core Design

Our first and foremost thing to concentrate before an automation run starts will be as follows:
* Properties Handling
* Driver Initialisation and Thread management
* Locator reference caching

Down below is a small flow diagram that sketches out the design for some quick reference

![Alt text](imgs/Flow diagram.png?raw=true "Optional Title")

#### Properties Handling

The properties handler consists of a Properties Manager whose roles are as follows:
* Read all Properties one by one and check if all the Mandatory properties are 
provided before the run starts.
* Throw **MissingMandatoryPropertyException** in case of any missing mandatory properties.
* Loads all properties into an in-memory cache if all the above checks succeeds.
* Servers as a one-stop point to provide necessary properties during runtime.

Here if we check the file PropsRepo, all the properties are grouped together in their 
own inner classes with respect to the property roles and are annotated with @Mandatory annotation.

Here's an example below:

https://github.com/cyber-noob/SAHybridFramework/blob/3858564692b171db3222ec939b246146c34798a5/src/main/java/org/aj/application/configs/PropsRepo.java#L2

Here you might also take note that we can define the default value of a variable but 
keep in mind that any sys prop with the same name will override the default values!

#### Driver Initialisation and Thread management

The Driver Manager is responsible for the following:
* Create appium servers per client if the server doesn't exist using any free ports.
* Create necessary Drivers by fetching capabilities from capabilities manager.
* Bind the created drivers to respective threads.
* Always make sure that the thread instance has a driver associated with it along 
with a running server during run-time

#### Capabilities Manager

It has the following responsibilities:
* Verify if the provided value has the expected dtype.
* Never need to reference docs for capabilities as the interface provides a neat 
implementation over avaialble capabilities with respect to platforms.

#### Locator Reference Caching

Once all the above steps are successful, the locator caching mechanism kicks in 
that first checks if all the required locator files for the current automation run is present or not.
Once the condition is satisfied all the locator files are rendered and stored in an in-memory cache.

### Test Level Integration

A typical test case integration consists of following sections:
* Locator files
* Pages
* Business Logics
* Test files

#### Locator Files

Inspired from UI development frameworks, We have two types of json files:
* Components.json -> A special file that contains UI components that's common across pages
* Page.json -> Files that contain page specific locator info.

The json files are structured in the following way

```json
{
    "comment": "UI component name", 
    "Bar": {
        
        "comment": "Sub category of ui component",
        "TopBar": {
            
            "comment": "UI component category",
            "InputTextField": {
                
                "comment": "UI component",
                "SearchBar": {
                    
                    "comment": "Platform divisions",
                    "android": {
                        "id": "something"
                    },
                    
                    "ios": {
                        "accessibilityid": "something"
                    },
                    
                    "web": {
                        "xpath": "something"
                    }
                }
            }
        }
    }
}
```

At first the json might seem quite dense for a single locator but on scale you can group 
similar components together in a logical fasion thereby increasing readability and reduce locator 
duplication.

The following locator shall be read as **Bar.TopBar.InputTextField.SearchBar** and with the 
following json path itself one can easily deduce that the ui locator is an input text field and
it's a part of top bar elements!!!

#### Page Implementation

A typical page implementation is similar to industry standards but with a little twist.
Each page implementation is expected to implement only actions over a locator and logics are strictly not allowed.
Consider this like an atom, the smallest unit of your test case.

More over all the actions are grouped together in their own inner classes that corespond to a feature thereby 
again increasing code readability and reducing duplicates.

#### Business Logic Layer

This contains all the business logics and are structured exactly similar to page classes where every
inner class is a feature. We shall be using switch case in case of any minor changes in business logic flow for any specific platform.
This is a conscious decision over a typical Page Factory Design as the following solution provides the following advantages:
* A single class to deal with for every platform. (In Page Factory Design, the number of class will be equal to number of platforms thereby 
causing the code to be split across classes increasing maintanance work)
* Code reusability as the common logics remains the same
* Code logics wrt platforms grouped together in one place offering huge readability in terms of varying business requirements per platform and 
less code to maintain.

## FINALLY
### Test Case Files

Similar to page and business logic layer, Test classes are grouped wrt their features so that let's say in future are feature gets deprecated,
all it taskes is a single annotation to disable a bunch of testcases retaining clean code.

More over every testcase is annotated with the platform specific annotation so that we don't miss out running a testcase for a platform by a silly spelling mistake.
More over annotations provide great readability over using tags which will definitely become a chore in terms of maintenance as that will become an n-n mapping where  
n -> platforms  
n -> suite

Hope you liked the solution and the repo design. You can find all the core logics explained above in the core package.  
If you liked the project don't forget to support by leaving a star and I wish you great day and happy coding!!!

## Easy Setup

The Framework also has a configure.sh file that tries to automatically configure your local machine compatible for the repo but the shell script is still a work in progress.
Any interested candidates can reach me out at ajay0912.dpi@gmail.com for collaboration.