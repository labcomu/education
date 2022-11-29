#!/bin/bash

echo "Must be executed on the project root directory\n"

ORCID_SERVICE_PORT_1=$(docker-compose port --index=1 orcid-service 8181 | cut -d ":" -f2 )
ORCID_SERVICE_PORT_2=$(docker-compose port --index=2 orcid-service 8181 | cut -d ":" -f2 )
ORG_SERVICE_PORT=8281
EDU_SERVICE_PORT=8381

do_common() {

  echo
  echo
  echo "organization | org-service"
  curl --location --request GET "http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/ufrn.br"

  echo
  echo
  echo "researcher | org-service"
  curl --location --request POST "http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/researcher/ufrn.br" \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "orcid": "0000-0002-5467-6458"
  }'

  echo
  echo
  echo "organization | org-service"
  curl --location --request GET "http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/ufrn.br"

  echo
  echo
  echo "organization | edu-service"
  seq 1 10 | xargs -n1 -P10 curl --location --request GET "http://localhost:${EDU_SERVICE_PORT}/api/v1/edu/organization/ufrn.br"

  echo
  echo
}

do_local() {
  echo
  echo
  echo "organization | orcid-service"
  curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/active"

  echo
  echo
  echo "organization | orcid-service"
  curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/researcher/0000-0002-2102-8577"

  do_common
}

do_docker() {
  echo
  echo
  echo "organization | orcid-service replica 1"
  #echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT_1}/api/v1/orcid/active\" -> change port to dynamically assigned from Docker"
  seq 1 5 | xargs -n1 -P10 curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT_1}/api/v1/orcid/active"

  echo
  echo
  echo "organization | orcid-service replica 1"
  #echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT_1}/api/v1/orcid/researcher/0000-0002-2102-8577\" -> change port to dynamically assigned from Docker"
  seq 1 5 | xargs -n1 -P10 curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT_1}/api/v1/orcid/researcher/0000-0002-2102-8577"

  echo
  echo
  echo "organization | orcid-service replica 2"
  #echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT_2}/api/v1/orcid/active\" -> change port to dynamically assigned from Docker"
  seq 1 5 | xargs -n1 -P10 curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT_2}/api/v1/orcid/active"

  echo
  echo
  echo "organization | orcid-service replica 2"
  #echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT_2}/api/v1/orcid/researcher/0000-0002-2102-8577\" -> change port to dynamical>
  seq 1 5 | xargs -n1 -P5 curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT_2}/api/v1/orcid/researcher/0000-0002-2102-8577"


  do_common
}

. ./do.sh Test $@
