#!/bin/bash

ORCID_SERVICE_PORT=8181
ORG_SERVICE_PORT=8281
EDU_SERVICE_PORT=8381


do_common() {

  echo
  echo
  echo "organization | org-service"
  echo "curl --location --request GET \"http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/ufrn.br\""
  curl --location --request GET "http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/ufrn.br"

  echo
  echo
  echo "researcher | org-service"
  echo "curl --location --request POST \"http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/researcher/ufrn.br\" --header 'Content-Type: application/json' --data-raw '{ \"orcid\": \"0000-0002-5467-6458\" }'"
  curl --location --request POST "http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/researcher/ufrn.br" \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "orcid": "0000-0002-5467-6458"
  }'

  echo
  echo
  echo "organization | org-service"
  echo "curl --location --request GET \"http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/ufrn.br\""
  curl --location --request GET "http://localhost:${ORG_SERVICE_PORT}/api/v1/org/organization/ufrn.br"

  echo
  echo
  echo "organization | edu-service"
  echo "curl --location --request GET \"http://localhost:${EDU_SERVICE_PORT}/api/v1/edu/organization/ufrn.br\""
  curl --location --request GET "http://localhost:${EDU_SERVICE_PORT}/api/v1/edu/organization/ufrn.br"

  echo
  echo
}

do_local() {
  echo
  echo "organization | orcid-service"
  echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/active\""
  curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/active"

  echo
  echo
  echo "organization | orcid-service"
  echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/researcher/0000-0002-2102-8577\""
  curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/researcher/0000-0002-2102-8577"

  do_common
}

do_docker() {
  echo
  echo "organization | orcid-service"
  echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/active\" -> change port to dynamically assigned from Docker"
  # curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/active"

  echo
  echo
  echo "organization | orcid-service"
  echo "curl --location --request GET \"http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/researcher/0000-0002-2102-8577\" -> change port to dynamically assigned from Docker"
  # curl --location --request GET "http://localhost:${ORCID_SERVICE_PORT}/api/v1/orcid/researcher/0000-0002-2102-8577"

  do_common
}

. ./do.sh Test $@