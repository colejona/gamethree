# GameThree

## Setup
_Setup assumes Mac development env. See the attached resources for a non-Mac_
1. Ensure [Homebrew](https://brew.sh) is installed
1. `brew install yarn`
    1. _Not-required if Yarn is already on system_
1. `yarn install`
> **TODO**: add Clojure package install command

### Additional Setup Resources
1. **Yarn**: https://yarnpkg.com/lang/en/docs/install
1. **ClojureScript**: https://clojurescript.org/guides/quick-start
1. **Shadow ClojureScript**: https://shadow-cljs.github.io/docs/UsersGuide.html

## Running
1. `yarn start` runs the server and serves down client files

## Development
1. `yarn update-server` - rebuilds the server code and starts the server
1. `yarn build-client` - rebuilds the client code and copies the static resources
1. `yarn build-server` - rebuilds the server code

## Resources
1. **Repo**: https://github.com/colejona/gamethree
1. **Heroku App**: https://gamethree.herokuapp.com
1. **Phaser Library**: https://github.com/photonstorm/phaser
