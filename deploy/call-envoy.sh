#!/bin/bash

#TOKEN=""
TOKEN=$(./keycloak-tokens.sh)

#curl -H "Authorization: Bearer ${TOKEN}" \
#  -H "X-Request-ID: 1234" \
#  -H "x-client-request-id: 1235" \
#  -H "Content-Type: application/json" \
#  http://localhost:8090/v1/create \
#  -d '{"debug":{"mode":"stub","stub":"success"},"event":{"title":"my title","description":"my description","visibility":"public"}}'

curl -H "Authorization: Bearer ${TOKEN}" \
  -H "X-Request-ID: 1234" \
  -H "x-client-request-id: 1235" \
  http://localhost:8090/
