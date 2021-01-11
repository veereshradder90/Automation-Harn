package io.harness.rest.helper.cdngdx.connectors;

public class ConnectorsConstants {
    public static final String CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accounts}/connectors";
    public static final String GET_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accountIdentifier}/connectors/{connectors}";
    public static final String GET_ORG_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accountIdentifier}/connectors/{connectors}?orgIdentifier={orgIdentifier}";
    public static final String GET_PROJ_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accountIdentifier}/connectors/{connectors}?orgIdentifier={orgIdentifier}&projectIdentifier={projectIdentifier}";

    public static final String LIST_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accountIdentifier}/connectors";
    public static final String LIST_ORG_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accountIdentifier}/connectors?orgIdentifier={orgIdentifier}";
    public static final String LIST_PROJ_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accountIdentifier}/connectors?orgIdentifier={orgIdentifier}&projectIdentifier={projectIdentifier}";

    public static final String DELETE_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accounts}/connectors/{connectors}";
    public static final String DELETE_ORG_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accounts}/connectors/{connectors}?orgIdentifier={orgIdentifier}";
    public static final String DELETE_PROJ_CONNECTOR_END_POINT ="https://dev.harness.io/cd/api/accounts/{accounts}/connectors/{connectors}?orgIdentifier={orgIdentifier}&projectIdentifier={projectIdentifier}";

}



