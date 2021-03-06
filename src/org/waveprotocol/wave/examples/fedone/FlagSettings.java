/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.waveprotocol.wave.examples.fedone;

/**
 * Flags configuration for WaveServer Module.
 *
 *
 */
// TODO - add descriptions to all flags.
public class FlagSettings {
  @Flag(name="xmpp_server_hostname")
  private static String xmppServerHostname;

  @Flag(name="xmpp_server_secret")
  private static String xmppServerSecret;

  @Flag(name="xmpp_component_name")
  private static String xmppComponentName;

  @Flag(name="xmpp_server_port")
  private static int xmppServerPort;

  @Flag(name="xmpp_server_ip")
  private static String xmppServerIp;

  @Flag(name="xmpp_server_ping")
  private static String xmppServerPing;

  @Flag(name="client_frontend_hostname")
  private static String clientFrontendHost;

  @Flag(name="client_frontend_port")
  private static String clientFrontEndPort;

  @Flag(name="websocket_frontend_hostname", defaultValue="localhost")
  private static String websocketFrontendHost;

  @Flag(name="websocket_frontend_port", defaultValue="9898")
  private static int websocketFrontEndPort;

  @Flag(name="certificate_private_key")
  private static String certificatePrivKey;

  @Flag(name="certificate_files", description="comma separated WITH NO SPACES.")
  private static String certificateFiles;

  @Flag(name="certificate_domain")
  private static String certificateDomain;

  @Flag(name="waveserver_disable_verification")
  private static boolean waveserverDisableVerification;

  @Flag(name="waveserver_disable_signer_verification")
  private static boolean waveserverDisableSignerVerification;

  @Flag(name="xmpp_server_description")
  private static String xmppServerDescription;

  @Flag(name="xmpp_jid")
  private static String xmppJid;

  @Flag(name="delta_bundling_accumulation_delay_ms", description="The delay, in ms, during" +
  		" which to accumulate deltas in a bundle before singing it. Value must be >= 0 ms")
  private static int deltaBundlingAccumulationDelay;
  @Flag(name="maximum_delta_bundle_size", description="The largest bundle size to accumulate before " +
  		"signing. If set to 1, will do straightforward delta signing.")
private static int deltaBundleSize;
  /*set to true to enable or false to disable persistence*/
  @Flag(name="waveserver_enable_persistence", defaultValue="false")
  private static boolean waveserverEnablePersistence;
}
