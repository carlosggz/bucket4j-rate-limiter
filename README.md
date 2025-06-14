# Bucket4j rate limiter

Example of how to use the rate limiter with the token bucket algorithm 
using the bucket4j library.

This demo contains two use cases:
- manually check the rate limiter
- automatically checks the rate limiter using an interceptor

The rates are defined in the settings and grouped by clients. The client is 
sent in every request as a header.

## How to run

### Before start
- Start docker engine
- Execute docker compose to start the redis server to share the rate limiter
  settings between instances. 

   ````
   docker compose up
   ````

- Initialize docker swarm cluster

    ````
    docker swarm init
    ````

### Steps
- Run the start multi instance script:

  ````
  sh docker/run-instances.sh
  ````

- Execute the script to emulate the http traffic:

  ````
  cd src/trigger
  npm install 
  npm run start
  ````


The first 100 request will be accepted with code 200 and the rest will
be returned with code 429

### After test completed 
- Run the stop script:

  ``sh docker/stop-instances.sh``

- Leave the swarm cluster

  ``docker swarm leave --force``

- Stop the redis service

  ``docker compose down``

