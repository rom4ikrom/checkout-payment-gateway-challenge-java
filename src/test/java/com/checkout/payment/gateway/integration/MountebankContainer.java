package com.checkout.payment.gateway.integration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

public class MountebankContainer extends GenericContainer<MountebankContainer> {

  private static final String IMAGE = "bbyars/mountebank:2.8.1";
  private static final int HTTP_PORT = 8080;

  private static MountebankContainer INSTANCE;

  private MountebankContainer() {
    super(IMAGE);
    withExposedPorts(HTTP_PORT);
    withCopyFileToContainer(MountableFile.forHostPath("imposters/bank_simulator.ejs"),
        "/imposters/bank_simulator.ejs");
    withCommand(
        "--configfile",
        "/imposters/bank_simulator.ejs",
        "--allowInjection"
    );
  }

  public static MountebankContainer instance() {
    if (INSTANCE == null) {
      return new MountebankContainer();
    }
    return INSTANCE;
  }

  public String url() {
    return "http://" + getHost() + ":" + getMappedPort(HTTP_PORT);
  }

}
