# GameThree

## Setup
_Setup assumes Mac development env. See the attached resources for a non-Mac_
1. Ensure [Homebrew](https://brew.sh) is installed
1. Install Java 8: https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html
1. `brew install yarn`
    1. _Not-required if Yarn is already on system_
1. `yarn install`
    1. Installs the Node dependencies
1. `yarn build`
    1. Installs the Clojure dependencies and compiles the ClojureScript to JavaScript

### Additional Setup Resources
1. **Yarn**: https://yarnpkg.com/lang/en/docs/install
1. **ClojureScript**: https://clojurescript.org/guides/quick-start
1. **Shadow ClojureScript**: https://shadow-cljs.github.io/docs/UsersGuide.html

## Running
1. `yarn start` runs the server and serves down client files

## Development
1. *Recommended Dev Command*: `yarn live` - run live reload (server + client files will auto-refresh, but still requires a browser reload to get changes)
1. `yarn update-server` - rebuilds the server code and starts the server
1. `yarn build-client` - rebuilds the client code and copies the static resources
1. `yarn build-server` - rebuilds the server code

### Heroku
1. Make sure your Heroku app has the following build packs installed:
    1. `heroku/node-js`
    1. https://github.com/heroku/heroku-buildpack-jvm-common.git
        1. Provides `jvm` support to the Heroku instance (required to compile ClojureScript to JS)
        1. Reference: https://help.heroku.com/2FSHO0RR/how-can-i-add-java-to-a-non-java-app

## Resources
1. **Repo**: https://github.com/colejona/gamethree
1. **Heroku App**: https://gamethree.herokuapp.com
1. **Phaser Library**: https://github.com/photonstorm/phaser
