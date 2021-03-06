#!/bin/bash

# This script will start the FedOne wave server.
#

if [ -f run-config.sh ] ; then
  . run-config.sh
else
  echo "You need to copy run-config.sh.example to run-config.sh and configure" ; exit 1
fi

exec java -jar dist/fedone-server-$FEDONE_VERSION.jar \
  --client_frontend_hostname=$WAVE_SERVER_HOSTNAME \
  --client_frontend_port=$WAVE_SERVER_PORT \
  --xmpp_component_name=wave \
  --xmpp_jid=wave.${WAVE_SERVER_DOMAIN_NAME} \
  --xmpp_server_description="FedOne" \
  --xmpp_server_hostname=$XMPP_SERVER_HOSTNAME \
  --xmpp_server_ip=$XMPP_SERVER_IP \
  --xmpp_server_port=$XMPP_SERVER_PORT \
  --xmpp_server_secret $XMPP_SERVER_SECRET \
  --xmpp_server_ping=$XMPP_SERVER_PING \
  --certificate_private_key=$PRIVATE_KEY_FILENAME \
  --certificate_files=$CERTIFICATE_FILENAME_LIST \
  --certificate_domain=$CERTIFICATE_DOMAIN_NAME \
  --delta_bundling_accumulation_delay_ms=0 \
  --maximum_delta_bundle_size=1 \
  --waveserver_disable_verification=$WAVESERVER_DISABLE_VERIFICATION \
  --waveserver_disable_signer_verification=$WAVESERVER_DISABLE_SIGNER_VERIFICATION \
  --websocket_frontend_hostname=$WEBSOCKET_SERVER_HOSTNAME \
  --websocket_frontend_port=$WEBSOCKET_SERVER_PORT
