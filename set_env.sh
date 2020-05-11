#!/usr/bin/env bash

case $TEST_ENV in
  qca01 | qa | ujetqa)
    domain="cb.ujetqa.co"
    ;;

  tst01 | pr | ujetpr)
    domain="cb.ujetpr.co"
    ;;

  rel01 | rel02 | ujetrc)
    domain="cb.ujetrc.co"
    ;;

  stg01 | stg02 | ujetst)
    domain="cb.ujetst.co"
    ;;

  prj)
    domain="prj${TEST_COMPANY:0:2}.dev.ujet.xyz"
    export TEST_COMPANY=${TEST_COMPANY:2}
    ;;

  *)
    domain="$TEST_ENV.ujet.co"
    ;;
esac

ADMIN_PORTAL_URL="https://${TEST_COMPANY}.${domain}"

echo "Admin portal url: $ADMIN_PORTAL_URL"
echo $ADMIN_PORTAL_URL > "$(pwd)/admin_portal_url"
