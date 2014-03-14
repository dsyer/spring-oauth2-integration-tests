This project shows what you can do with the minimum configuration to
set up an Authorization Server and Resource Server using Json Web
Tokens (JWT) as the bearer token format. This means that the tokens
themselves contain all the information about the grant (permissions
etc.) and there is no need for state on the server.

For the Authorization Server, in addition to the basic "vanilla"
features, you need create and inject a `JwtTokenServices`. You can see
this is the bulk of `Application.java`.

The Resource Server is similarly easy: in addition to the basic
"vanilla" features, you need create and inject a `JwtTokenServices`.
In addition, in this app we don't change the default protected
resource patterns, but we do add an access rule (scope='read' is
required to access all resources).
