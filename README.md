# "Object tree" scope

This is a prototype of a CDI custom scope which would allow to share beans with `@TreeDependent` scope across the object tree of a bean annotated with interceptor binding `@TreeRoot`.
Note it is not possible to inject a `@TreeDependent` bean outside the current object tree (e.g. into `@Singleton` or `@ApplicationScoped` bean).
In other words, you can only inject a `@TreeDependent` bean into `@Dependent` or `@TreeDependent` beans.
See also the `TreeContextTest` test case. For `Root`, `Bloom` and `Reused` with `@Dependent` scope we get two instances of `Reused` in the tree:
````
Root
   L Bloom
         L Reused (1)
   L Reused (2)
````
For `Root`, `Bloom` with `@Dependent` and `Reused` with `@TreeDependent` we get one shared instance of `Reused` in our tree. This instance is actually a dependent of the `Root`:
````
Root
   L Bloom
     |
   L Reused
````

