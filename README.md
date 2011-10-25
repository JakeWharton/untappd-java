untappd-java
============

A Java wrapper around the [Untappd RESTful API][1] and a simple DSL for easy
interaction.

Remote services are grouped into local service objects which can be centrally
managed by a `ServiceManager` instance. The manager will act as a factory for
all of the services and will automatically initialize them with your
credentials and API key.

Each service contains methods which correspond to a remote method. Each of
these methods instantiates a class that will allow for you to build the
parameters using the Java builder pattern.

Required remote method parameters will be arguments to the service method and
all of the methods in the returned builder are optional.

When fully assembled, you can trigger the remote execution by calling the
`fire()` method. This will return a native object which represents the result
of the execution. All returned objects are immutable and should be handled
as such.


Usage
=====

Coming soon...


Documentation
-------------

 * Javadocs are available at [jakewharton.github.com/untappd-java/][2].
 * Repository is hosted on [github.com/JakeWharton/untappd-java/][3].
 * Native API is documented on [untappd.com/api/docs][1].

Questions, comments, and suggestions should be posted on the [Google Group][5]
for Jake Wharton's projects.



Developed By
============

* Jake Wharton - <jakewharton@gmail.com>


Contributors
------------

The overall package and class layout as well as approximately 50% of the code
in the `Untappd*` classes are based on [nabeelmukhtar's github-java-sdk
library][4].



License
=======

    Copyright 2011 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




 [1]: http://untappd.com/api/docs/
 [2]: http://jakewharton.github.com/untappd-java/
 [3]: https://github.com/JakeWharton/untappd-java/
 [4]: https://github.com/nabeelmukhtar/github-java-sdk
 [5]: https://groups.google.com/forum/#!forum/jakewharton-projects
