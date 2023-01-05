/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.extensions;

<<<<<<< HEAD
=======
import static java.util.Collections.emptyList;
>>>>>>> main
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.opensearch.test.ClusterServiceUtils.createClusterService;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
<<<<<<< HEAD
=======
import java.util.stream.Collectors;
>>>>>>> main

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.opensearch.Version;
import org.opensearch.action.admin.cluster.state.ClusterStateResponse;
<<<<<<< HEAD
import org.opensearch.cluster.ClusterSettingsResponse;
import org.opensearch.cluster.LocalNodeResponse;
=======
import org.opensearch.client.node.NodeClient;
import org.opensearch.cluster.ClusterSettingsResponse;
import org.opensearch.env.EnvironmentSettingsResponse;
>>>>>>> main
import org.opensearch.cluster.metadata.IndexMetadata;
import org.opensearch.cluster.metadata.IndexNameExpressionResolver;
import org.opensearch.cluster.node.DiscoveryNode;
import org.opensearch.cluster.service.ClusterService;
<<<<<<< HEAD
import org.opensearch.common.io.PathUtils;
import org.opensearch.common.io.stream.NamedWriteableRegistry;
import org.opensearch.common.network.NetworkService;
import org.opensearch.common.settings.Settings;
=======
import org.opensearch.common.bytes.BytesReference;
import org.opensearch.common.io.PathUtils;
import org.opensearch.common.io.stream.BytesStreamInput;
import org.opensearch.common.io.stream.BytesStreamOutput;
import org.opensearch.common.io.stream.NamedWriteableRegistry;
import org.opensearch.common.network.NetworkService;
import org.opensearch.common.settings.Setting;
import org.opensearch.common.settings.Settings;
import org.opensearch.common.settings.WriteableSetting;
import org.opensearch.common.settings.Setting.Property;
import org.opensearch.common.settings.WriteableSetting.SettingType;
import org.opensearch.common.settings.SettingsModule;
>>>>>>> main
import org.opensearch.common.transport.TransportAddress;
import org.opensearch.common.util.FeatureFlagTests;
import org.opensearch.common.util.PageCacheRecycler;
import org.opensearch.common.util.concurrent.ThreadContext;
import org.opensearch.env.Environment;
import org.opensearch.env.TestEnvironment;
<<<<<<< HEAD
=======
import org.opensearch.extensions.rest.RegisterRestActionsRequest;
import org.opensearch.extensions.settings.RegisterCustomSettingsRequest;
>>>>>>> main
import org.opensearch.index.IndexModule;
import org.opensearch.index.IndexSettings;
import org.opensearch.index.analysis.AnalysisRegistry;
import org.opensearch.index.engine.EngineConfigFactory;
import org.opensearch.index.engine.InternalEngineFactory;
import org.opensearch.indices.breaker.NoneCircuitBreakerService;
import org.opensearch.plugins.PluginInfo;
<<<<<<< HEAD
import org.opensearch.test.IndexSettingsModule;
import org.opensearch.test.MockLogAppender;
import org.opensearch.test.OpenSearchTestCase;
import org.opensearch.test.transport.MockTransportService;
import org.opensearch.threadpool.TestThreadPool;
import org.opensearch.threadpool.ThreadPool;
import org.opensearch.transport.ConnectTransportException;
import org.opensearch.transport.Transport;
import org.opensearch.transport.TransportService;
import org.opensearch.transport.nio.MockNioTransport;
=======
import org.opensearch.rest.RestController;
import org.opensearch.test.IndexSettingsModule;
import org.opensearch.test.MockLogAppender;
import org.opensearch.test.OpenSearchTestCase;
import org.opensearch.test.client.NoOpNodeClient;
import org.opensearch.test.transport.MockTransportService;
import org.opensearch.threadpool.TestThreadPool;
import org.opensearch.threadpool.ThreadPool;
import org.opensearch.transport.Transport;
import org.opensearch.transport.TransportResponse;
import org.opensearch.transport.TransportService;
import org.opensearch.transport.nio.MockNioTransport;
import org.opensearch.usage.UsageService;
>>>>>>> main

public class ExtensionsManagerTests extends OpenSearchTestCase {

    private TransportService transportService;
<<<<<<< HEAD
    private ClusterService clusterService;
    private MockNioTransport transport;
=======
    private RestController restController;
    private SettingsModule settingsModule;
    private ClusterService clusterService;
    private NodeClient client;
    private MockNioTransport transport;
    private Path extensionDir;
>>>>>>> main
    private final ThreadPool threadPool = new TestThreadPool(ExtensionsManagerTests.class.getSimpleName());
    private final Settings settings = Settings.builder()
        .put(IndexMetadata.SETTING_VERSION_CREATED, Version.CURRENT)
        .put(Environment.PATH_HOME_SETTING.getKey(), createTempDir().toString())
        .build();
<<<<<<< HEAD
=======
    private final List<String> extensionsYmlLines = Arrays.asList(
        "extensions:",
        "   - name: firstExtension",
        "     uniqueId: uniqueid1",
        "     hostName: 'myIndependentPluginHost1'",
        "     hostAddress: '127.0.0.0'",
        "     port: '9300'",
        "     version: '0.0.7'",
        "     description: Fake description 1",
        "     opensearchVersion: '3.0.0'",
        "     javaVersion: '14'",
        "     className: fakeClass1",
        "     customFolderName: fakeFolder1",
        "     hasNativeController: false",
        "   - name: secondExtension",
        "     uniqueId: 'uniqueid2'",
        "     hostName: 'myIndependentPluginHost2'",
        "     hostAddress: '127.0.0.1'",
        "     port: '9301'",
        "     version: '3.14.16'",
        "     description: Fake description 2",
        "     opensearchVersion: '2.0.0'",
        "     javaVersion: '17'",
        "     className: fakeClass2",
        "     customFolderName: fakeFolder2",
        "     hasNativeController: true",
        "     dependencies:",
        "       - uniqueId: 'uniqueid0'",
        "       - version: '2.0.0'"
    );

    private DiscoveryExtensionNode extensionNode;
>>>>>>> main

    @Before
    public void setup() throws Exception {
        FeatureFlagTests.enableFeature();
        Settings settings = Settings.builder().put("cluster.name", "test").build();
        transport = new MockNioTransport(
            settings,
            Version.CURRENT,
            threadPool,
            new NetworkService(Collections.emptyList()),
            PageCacheRecycler.NON_RECYCLING_INSTANCE,
            new NamedWriteableRegistry(Collections.emptyList()),
            new NoneCircuitBreakerService()
        );
        transportService = new MockTransportService(
            settings,
            transport,
            threadPool,
            TransportService.NOOP_TRANSPORT_INTERCEPTOR,
            (boundAddress) -> new DiscoveryNode(
                "test_node",
                "test_node",
                boundAddress.publishAddress(),
                emptyMap(),
                emptySet(),
                Version.CURRENT
            ),
            null,
            Collections.emptySet()
        );
<<<<<<< HEAD
        clusterService = createClusterService(threadPool);
    }

=======
        restController = new RestController(
            emptySet(),
            null,
            new NodeClient(Settings.EMPTY, threadPool),
            new NoneCircuitBreakerService(),
            new UsageService()
        );
        settingsModule = new SettingsModule(Settings.EMPTY, emptyList(), emptyList(), emptySet());
        clusterService = createClusterService(threadPool);

        extensionDir = createTempDir();

        extensionNode = new DiscoveryExtensionNode(
            "firstExtension",
            "uniqueid1",
            "uniqueid1",
            "myIndependentPluginHost1",
            "127.0.0.0",
            new TransportAddress(InetAddress.getByName("127.0.0.0"), 9300),
            new HashMap<String, String>(),
            Version.fromString("3.0.0"),
            new PluginInfo(
                "firstExtension",
                "Fake description 1",
                "0.0.7",
                Version.fromString("3.0.0"),
                "14",
                "fakeClass1",
                new ArrayList<String>(),
                false
            ),
            Collections.emptyList()
        );
        client = new NoOpNodeClient(this.getTestName());
    }

    @Override
>>>>>>> main
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        transportService.close();
<<<<<<< HEAD
        ThreadPool.terminate(threadPool, 30, TimeUnit.SECONDS);
    }

    public void testExtensionsDiscovery() throws Exception {
        Path extensionDir = createTempDir();

        List<String> extensionsYmlLines = Arrays.asList(
            "extensions:",
            "   - name: firstExtension",
            "     uniqueId: uniqueid1",
            "     hostName: 'myIndependentPluginHost1'",
            "     hostAddress: '127.0.0.0'",
            "     port: '9300'",
            "     version: '0.0.7'",
            "     description: Fake description 1",
            "     opensearchVersion: '3.0.0'",
            "     javaVersion: '14'",
            "     className: fakeClass1",
            "     customFolderName: fakeFolder1",
            "     hasNativeController: false",
            "   - name: secondExtension",
            "     uniqueId: 'uniqueid2'",
            "     hostName: 'myIndependentPluginHost2'",
            "     hostAddress: '127.0.0.1'",
            "     port: '9301'",
            "     version: '3.14.16'",
            "     description: Fake description 2",
            "     opensearchVersion: '2.0.0'",
            "     javaVersion: '17'",
            "     className: fakeClass2",
            "     customFolderName: fakeFolder2",
            "     hasNativeController: true"
        );
=======
        client.close();
        ThreadPool.terminate(threadPool, 30, TimeUnit.SECONDS);
    }

    public void testDiscover() throws Exception {
>>>>>>> main
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);

        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);

        List<DiscoveryExtensionNode> expectedUninitializedExtensions = new ArrayList<DiscoveryExtensionNode>();

<<<<<<< HEAD
=======
        String expectedUniqueId = "uniqueid0";
        Version expectedVersion = Version.fromString("2.0.0");
        ExtensionDependency expectedDependency = new ExtensionDependency(expectedUniqueId, expectedVersion);

>>>>>>> main
        expectedUninitializedExtensions.add(
            new DiscoveryExtensionNode(
                "firstExtension",
                "uniqueid1",
                "uniqueid1",
                "myIndependentPluginHost1",
                "127.0.0.0",
                new TransportAddress(InetAddress.getByName("127.0.0.0"), 9300),
                new HashMap<String, String>(),
                Version.fromString("3.0.0"),
                new PluginInfo(
                    "firstExtension",
                    "Fake description 1",
                    "0.0.7",
                    Version.fromString("3.0.0"),
                    "14",
                    "fakeClass1",
                    new ArrayList<String>(),
                    false
<<<<<<< HEAD
                )
=======
                ),
                Collections.emptyList()
>>>>>>> main
            )
        );

        expectedUninitializedExtensions.add(
            new DiscoveryExtensionNode(
                "secondExtension",
                "uniqueid2",
                "uniqueid2",
                "myIndependentPluginHost2",
                "127.0.0.1",
                new TransportAddress(TransportAddress.META_ADDRESS, 9301),
                new HashMap<String, String>(),
                Version.fromString("2.0.0"),
                new PluginInfo(
                    "secondExtension",
                    "Fake description 2",
                    "3.14.16",
                    Version.fromString("2.0.0"),
                    "17",
                    "fakeClass2",
                    new ArrayList<String>(),
                    true
<<<<<<< HEAD
                )
            )
        );
        assertEquals(expectedUninitializedExtensions, extensionsManager.getUninitializedExtensions());
=======
                ),
                List.of(expectedDependency)
            )
        );
        assertEquals(expectedUninitializedExtensions.size(), extensionsManager.getExtensionIdMap().values().size());
        assertEquals(List.of(expectedDependency), expectedUninitializedExtensions.get(1).getDependencies());
        assertTrue(expectedUninitializedExtensions.containsAll(extensionsManager.getExtensionIdMap().values()));
        assertTrue(extensionsManager.getExtensionIdMap().values().containsAll(expectedUninitializedExtensions));
    }

    public void testNonUniqueExtensionsDiscovery() throws Exception {
        Path emptyExtensionDir = createTempDir();
        List<String> nonUniqueYmlLines = extensionsYmlLines.stream()
            .map(s -> s.replace("uniqueid2", "uniqueid1"))
            .collect(Collectors.toList());
        Files.write(emptyExtensionDir.resolve("extensions.yml"), nonUniqueYmlLines, StandardCharsets.UTF_8);

        ExtensionsManager extensionsManager = new ExtensionsManager(settings, emptyExtensionDir);

        List<DiscoveryExtensionNode> expectedUninitializedExtensions = new ArrayList<DiscoveryExtensionNode>();

        expectedUninitializedExtensions.add(
            new DiscoveryExtensionNode(
                "firstExtension",
                "uniqueid1",
                "uniqueid1",
                "myIndependentPluginHost1",
                "127.0.0.0",
                new TransportAddress(InetAddress.getByName("127.0.0.0"), 9300),
                new HashMap<String, String>(),
                Version.fromString("3.0.0"),
                new PluginInfo(
                    "firstExtension",
                    "Fake description 1",
                    "0.0.7",
                    Version.fromString("3.0.0"),
                    "14",
                    "fakeClass1",
                    new ArrayList<String>(),
                    false
                ),
                Collections.emptyList()
            )
        );
        assertEquals(expectedUninitializedExtensions.size(), extensionsManager.getExtensionIdMap().values().size());
        assertTrue(expectedUninitializedExtensions.containsAll(extensionsManager.getExtensionIdMap().values()));
        assertTrue(extensionsManager.getExtensionIdMap().values().containsAll(expectedUninitializedExtensions));
        assertTrue(expectedUninitializedExtensions.containsAll(emptyList()));
    }

    public void testDiscoveryExtension() throws Exception {
        String expectedId = "test id";
        Version expectedVersion = Version.fromString("2.0.0");
        ExtensionDependency expectedDependency = new ExtensionDependency(expectedId, expectedVersion);

        DiscoveryExtensionNode discoveryExtensionNode = new DiscoveryExtensionNode(
            "firstExtension",
            "uniqueid1",
            "uniqueid1",
            "myIndependentPluginHost1",
            "127.0.0.0",
            new TransportAddress(InetAddress.getByName("127.0.0.0"), 9300),
            new HashMap<String, String>(),
            Version.fromString("3.0.0"),
            new PluginInfo(
                "firstExtension",
                "Fake description 1",
                "0.0.7",
                Version.fromString("3.0.0"),
                "14",
                "fakeClass1",
                new ArrayList<String>(),
                false
            ),
            List.of(expectedDependency)
        );

        assertEquals(List.of(expectedDependency), discoveryExtensionNode.getDependencies());

        try (BytesStreamOutput out = new BytesStreamOutput()) {
            discoveryExtensionNode.writeTo(out);
            out.flush();
            try (BytesStreamInput in = new BytesStreamInput(BytesReference.toBytes(out.bytes()))) {
                discoveryExtensionNode = new DiscoveryExtensionNode(in);

                assertEquals(List.of(expectedDependency), discoveryExtensionNode.getDependencies());
            }
        }
    }

    public void testExtensionDependency() throws Exception {
        String expectedUniqueId = "Test uniqueId";
        Version expectedVersion = Version.fromString("3.0.0");

        ExtensionDependency dependency = new ExtensionDependency(expectedUniqueId, expectedVersion);

        assertEquals(expectedUniqueId, dependency.getUniqueId());
        assertEquals(expectedVersion, dependency.getVersion());

        try (BytesStreamOutput out = new BytesStreamOutput()) {
            dependency.writeTo(out);
            out.flush();
            try (BytesStreamInput in = new BytesStreamInput(BytesReference.toBytes(out.bytes()))) {
                dependency = new ExtensionDependency(in);
                assertEquals(expectedUniqueId, dependency.getUniqueId());
                assertEquals(expectedVersion, dependency.getVersion());
            }
        }
>>>>>>> main
    }

    public void testNonAccessibleDirectory() throws Exception {
        AccessControlException e = expectThrows(

            AccessControlException.class,
            () -> new ExtensionsManager(settings, PathUtils.get(""))
        );
        assertEquals("access denied (\"java.io.FilePermission\" \"\" \"read\")", e.getMessage());
    }

    public void testNoExtensionsFile() throws Exception {
<<<<<<< HEAD
        Path extensionDir = createTempDir();

=======
>>>>>>> main
        Settings settings = Settings.builder().build();

        try (MockLogAppender mockLogAppender = MockLogAppender.createForLoggers(LogManager.getLogger(ExtensionsManager.class))) {

            mockLogAppender.addExpectation(
                new MockLogAppender.SeenEventExpectation(
                    "No Extensions File Present",
                    "org.opensearch.extensions.ExtensionsManager",
                    Level.INFO,
                    "Extensions.yml file is not present.  No extensions will be loaded."
                )
            );

            new ExtensionsManager(settings, extensionDir);

            mockLogAppender.assertAllExpectationsMatched();
        }
    }

    public void testEmptyExtensionsFile() throws Exception {
<<<<<<< HEAD
        Path extensionDir = createTempDir();

        List<String> extensionsYmlLines = Arrays.asList();
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);

        Settings settings = Settings.builder().build();

        expectThrows(IOException.class, () -> new ExtensionsManager(settings, extensionDir));
    }

    public void testInitialize() throws Exception {
        Path extensionDir = createTempDir();

        List<String> extensionsYmlLines = Arrays.asList(
            "extensions:",
            "   - name: firstExtension",
            "     uniqueId: uniqueid1",
            "     hostName: 'myIndependentPluginHost1'",
            "     hostAddress: '127.0.0.0'",
            "     port: '9300'",
            "     version: '0.0.7'",
            "     description: Fake description 1",
            "     opensearchVersion: '3.0.0'",
            "     javaVersion: '14'",
            "     className: fakeClass1",
            "     customFolderName: fakeFolder1",
            "     hasNativeController: false",
            "   - name: secondExtension",
            "     uniqueId: 'uniqueid2'",
            "     hostName: 'myIndependentPluginHost2'",
            "     hostAddress: '127.0.0.1'",
            "     port: '9301'",
            "     version: '3.14.16'",
            "     description: Fake description 2",
            "     opensearchVersion: '2.0.0'",
            "     javaVersion: '17'",
            "     className: fakeClass2",
            "     customFolderName: fakeFolder2",
            "     hasNativeController: true"
        );
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);

        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);

        transportService.start();
        transportService.acceptIncomingRequests();
        extensionsManager.setTransportService(transportService);

        expectThrows(ConnectTransportException.class, () -> extensionsManager.initialize());

    }

    public void testHandleExtensionRequest() throws Exception {

        Path extensionDir = createTempDir();

        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);

        extensionsManager.setTransportService(transportService);
        extensionsManager.setClusterService(clusterService);
        ExtensionRequest clusterStateRequest = new ExtensionRequest(ExtensionsManager.RequestType.REQUEST_EXTENSION_CLUSTER_STATE);
        assertEquals(extensionsManager.handleExtensionRequest(clusterStateRequest).getClass(), ClusterStateResponse.class);

        ExtensionRequest clusterSettingRequest = new ExtensionRequest(ExtensionsManager.RequestType.REQUEST_EXTENSION_CLUSTER_SETTINGS);
        assertEquals(extensionsManager.handleExtensionRequest(clusterSettingRequest).getClass(), ClusterSettingsResponse.class);

        ExtensionRequest localNodeRequest = new ExtensionRequest(ExtensionsManager.RequestType.REQUEST_EXTENSION_LOCAL_NODE);
        assertEquals(extensionsManager.handleExtensionRequest(localNodeRequest).getClass(), LocalNodeResponse.class);

        ExtensionRequest exceptionRequest = new ExtensionRequest(ExtensionsManager.RequestType.GET_SETTINGS);
        Exception exception = expectThrows(IllegalStateException.class, () -> extensionsManager.handleExtensionRequest(exceptionRequest));
        assertEquals(exception.getMessage(), "Handler not present for the provided request: " + exceptionRequest.getRequestType());
    }

    public void testRegisterHandler() throws Exception {
        Path extensionDir = createTempDir();
=======
        Path emptyExtensionDir = createTempDir();

        List<String> emptyExtensionsYmlLines = Arrays.asList();
        Files.write(emptyExtensionDir.resolve("extensions.yml"), emptyExtensionsYmlLines, StandardCharsets.UTF_8);

        Settings settings = Settings.builder().build();

        expectThrows(IOException.class, () -> new ExtensionsManager(settings, emptyExtensionDir));
    }

    public void testInitialize() throws Exception {
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);

        initialize(extensionsManager);

        try (MockLogAppender mockLogAppender = MockLogAppender.createForLoggers(LogManager.getLogger(ExtensionsManager.class))) {

            mockLogAppender.addExpectation(
                new MockLogAppender.SeenEventExpectation(
                    "Connect Transport Exception 1",
                    "org.opensearch.extensions.ExtensionsManager",
                    Level.ERROR,
                    "ConnectTransportException[[firstExtension][127.0.0.0:9300] connect_timeout[30s]]"
                )
            );

            mockLogAppender.addExpectation(
                new MockLogAppender.SeenEventExpectation(
                    "Connect Transport Exception 2",
                    "org.opensearch.extensions.ExtensionsManager",
                    Level.ERROR,
                    "ConnectTransportException[[secondExtension][127.0.0.1:9301] connect_exception]; nested: ConnectException[Connection refused];"
                )
            );

            extensionsManager.initialize();

            // Test needs to be changed to mock the connection between the local node and an extension. Assert statment is commented out for
            // now.
            // Link to issue: https://github.com/opensearch-project/OpenSearch/issues/4045
            // mockLogAppender.assertAllExpectationsMatched();
        }
    }

    public void testHandleRegisterRestActionsRequest() throws Exception {
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);

        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);

        String uniqueIdStr = "uniqueid1";
        List<String> actionsList = List.of("GET /foo", "PUT /bar", "POST /baz");
        RegisterRestActionsRequest registerActionsRequest = new RegisterRestActionsRequest(uniqueIdStr, actionsList);
        TransportResponse response = extensionsManager.getRestActionsRequestHandler()
            .handleRegisterRestActionsRequest(registerActionsRequest);
        assertEquals(AcknowledgedResponse.class, response.getClass());
        assertTrue(((AcknowledgedResponse) response).getStatus());
    }

    public void testHandleRegisterSettingsRequest() throws Exception {
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);

        String uniqueIdStr = "uniqueid1";
        List<Setting<?>> settingsList = List.of(
            Setting.boolSetting("index.falseSetting", false, Property.IndexScope, Property.Dynamic),
            Setting.simpleString("fooSetting", "foo", Property.NodeScope, Property.Final)
        );
        RegisterCustomSettingsRequest registerCustomSettingsRequest = new RegisterCustomSettingsRequest(uniqueIdStr, settingsList);
        TransportResponse response = extensionsManager.getCustomSettingsRequestHandler()
            .handleRegisterCustomSettingsRequest(registerCustomSettingsRequest);
        assertEquals(AcknowledgedResponse.class, response.getClass());
        assertTrue(((AcknowledgedResponse) response).getStatus());
    }

    public void testHandleRegisterRestActionsRequestWithInvalidMethod() throws Exception {
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);

        String uniqueIdStr = "uniqueid1";
        List<String> actionsList = List.of("FOO /foo", "PUT /bar", "POST /baz");
        RegisterRestActionsRequest registerActionsRequest = new RegisterRestActionsRequest(uniqueIdStr, actionsList);
        expectThrows(
            IllegalArgumentException.class,
            () -> extensionsManager.getRestActionsRequestHandler().handleRegisterRestActionsRequest(registerActionsRequest)
        );
    }

    public void testHandleRegisterRestActionsRequestWithInvalidUri() throws Exception {
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);
        String uniqueIdStr = "uniqueid1";
        List<String> actionsList = List.of("GET", "PUT /bar", "POST /baz");
        RegisterRestActionsRequest registerActionsRequest = new RegisterRestActionsRequest(uniqueIdStr, actionsList);
        expectThrows(
            IllegalArgumentException.class,
            () -> extensionsManager.getRestActionsRequestHandler().handleRegisterRestActionsRequest(registerActionsRequest)
        );
    }

    public void testHandleExtensionRequest() throws Exception {
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);

        ExtensionRequest clusterStateRequest = new ExtensionRequest(ExtensionsManager.RequestType.REQUEST_EXTENSION_CLUSTER_STATE);
        assertEquals(ClusterStateResponse.class, extensionsManager.handleExtensionRequest(clusterStateRequest).getClass());

        ExtensionRequest clusterSettingRequest = new ExtensionRequest(ExtensionsManager.RequestType.REQUEST_EXTENSION_CLUSTER_SETTINGS);
        assertEquals(ClusterSettingsResponse.class, extensionsManager.handleExtensionRequest(clusterSettingRequest).getClass());

        ExtensionRequest environmentSettingsRequest = new ExtensionRequest(
            ExtensionsManager.RequestType.REQUEST_EXTENSION_ENVIRONMENT_SETTINGS
        );
        assertEquals(EnvironmentSettingsResponse.class, extensionsManager.handleExtensionRequest(environmentSettingsRequest).getClass());

        ExtensionRequest exceptionRequest = new ExtensionRequest(ExtensionsManager.RequestType.GET_SETTINGS);
        Exception exception = expectThrows(
            IllegalArgumentException.class,
            () -> extensionsManager.handleExtensionRequest(exceptionRequest)
        );
        assertEquals("Handler not present for the provided request", exception.getMessage());
    }

    public void testEnvironmentSettingsResponse() throws Exception {

        // Test EnvironmentSettingsResponse arg constructor
        EnvironmentSettingsResponse environmentSettingsResponse = new EnvironmentSettingsResponse(settings);
        assertEquals(settings, environmentSettingsResponse.getEnvironmentSettings());

        // Test EnvironmentSettingsResponse StreamInput constructor
        try (BytesStreamOutput out = new BytesStreamOutput()) {
            environmentSettingsResponse.writeTo(out);
            out.flush();
            try (BytesStreamInput in = new BytesStreamInput(BytesReference.toBytes(out.bytes()))) {
                environmentSettingsResponse = new EnvironmentSettingsResponse(in);
                assertEquals(settings, environmentSettingsResponse.getEnvironmentSettings());
            }
        }

    }

    public void testEnvironmentSettingsRegisteredValue() throws Exception {
        // Create setting with value false
        Setting<Boolean> boolSetting = Setting.boolSetting("boolSetting", false, Property.Dynamic);

        // Create Settings with registered bool setting with value true
        Settings environmentSettings = Settings.builder().put("boolSetting", "true").build();

        EnvironmentSettingsResponse environmentSettingsResponse = new EnvironmentSettingsResponse(environmentSettings);
        try (BytesStreamOutput out = new BytesStreamOutput()) {
            environmentSettingsResponse.writeTo(out);
            out.flush();
            try (BytesStreamInput in = new BytesStreamInput(BytesReference.toBytes(out.bytes()))) {

                environmentSettingsResponse = new EnvironmentSettingsResponse(in);
                assertEquals(environmentSettings, environmentSettingsResponse.getEnvironmentSettings());

                // bool setting is registered in Settings object, thus the expected return value is the registered setting value
                assertEquals(true, boolSetting.get(environmentSettingsResponse.getEnvironmentSettings()));
            }
        }
    }

    public void testEnvironmentSettingsDefaultValue() throws Exception {
        // Create setting with value false
        Setting<Boolean> boolSetting = Setting.boolSetting("boolSetting", false, Property.Dynamic);

        // Create settings object without registered bool setting
        Settings environmentSettings = Settings.builder().put("testSetting", "true").build();

        EnvironmentSettingsResponse environmentSettingsResponse = new EnvironmentSettingsResponse(environmentSettings);
        try (BytesStreamOutput out = new BytesStreamOutput()) {
            environmentSettingsResponse.writeTo(out);
            out.flush();
            try (BytesStreamInput in = new BytesStreamInput(BytesReference.toBytes(out.bytes()))) {

                environmentSettingsResponse = new EnvironmentSettingsResponse(in);
                assertEquals(environmentSettings, environmentSettingsResponse.getEnvironmentSettings());
                // bool setting is not registered in Settings object, thus the expected return value is the default setting value
                assertEquals(false, boolSetting.get(environmentSettingsResponse.getEnvironmentSettings()));
            }
        }
    }

    public void testAddSettingsUpdateConsumerRequest() throws Exception {
        Path extensionDir = createTempDir();
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);

        List<Setting<?>> componentSettings = List.of(
            Setting.boolSetting("falseSetting", false, Property.IndexScope, Property.NodeScope),
            Setting.simpleString("fooSetting", "foo", Property.Dynamic)
        );

        // Test AddSettingsUpdateConsumerRequest arg constructor
        AddSettingsUpdateConsumerRequest addSettingsUpdateConsumerRequest = new AddSettingsUpdateConsumerRequest(
            extensionNode,
            componentSettings
        );
        assertEquals(extensionNode, addSettingsUpdateConsumerRequest.getExtensionNode());
        assertEquals(componentSettings.size(), addSettingsUpdateConsumerRequest.getComponentSettings().size());

        List<Setting<?>> requestComponentSettings = new ArrayList<>();
        for (WriteableSetting writeableSetting : addSettingsUpdateConsumerRequest.getComponentSettings()) {
            requestComponentSettings.add(writeableSetting.getSetting());
        }
        assertTrue(requestComponentSettings.containsAll(componentSettings));
        assertTrue(componentSettings.containsAll(requestComponentSettings));

        // Test AddSettingsUpdateConsumerRequest StreamInput constructor
        try (BytesStreamOutput out = new BytesStreamOutput()) {
            addSettingsUpdateConsumerRequest.writeTo(out);
            out.flush();
            try (BytesStreamInput in = new BytesStreamInput(BytesReference.toBytes(out.bytes()))) {
                addSettingsUpdateConsumerRequest = new AddSettingsUpdateConsumerRequest(in);
                assertEquals(extensionNode, addSettingsUpdateConsumerRequest.getExtensionNode());
                assertEquals(componentSettings.size(), addSettingsUpdateConsumerRequest.getComponentSettings().size());

                requestComponentSettings = new ArrayList<>();
                for (WriteableSetting writeableSetting : addSettingsUpdateConsumerRequest.getComponentSettings()) {
                    requestComponentSettings.add(writeableSetting.getSetting());
                }
                assertTrue(requestComponentSettings.containsAll(componentSettings));
                assertTrue(componentSettings.containsAll(requestComponentSettings));
            }
        }

    }

    public void testHandleAddSettingsUpdateConsumerRequest() throws Exception {

        Path extensionDir = createTempDir();
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);

        List<Setting<?>> componentSettings = List.of(
            Setting.boolSetting("falseSetting", false, Property.Dynamic),
            Setting.boolSetting("trueSetting", true, Property.Dynamic)
        );

        AddSettingsUpdateConsumerRequest addSettingsUpdateConsumerRequest = new AddSettingsUpdateConsumerRequest(
            extensionNode,
            componentSettings
        );
        TransportResponse response = extensionsManager.getAddSettingsUpdateConsumerRequestHandler()
            .handleAddSettingsUpdateConsumerRequest(addSettingsUpdateConsumerRequest);
        assertEquals(AcknowledgedResponse.class, response.getClass());
        // Should fail as component settings are not registered within cluster settings
        assertEquals(false, ((AcknowledgedResponse) response).getStatus());
    }

    public void testUpdateSettingsRequest() throws Exception {
        Path extensionDir = createTempDir();
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);

        Setting<?> componentSetting = Setting.boolSetting("falseSetting", false, Property.Dynamic);
        SettingType settingType = SettingType.Boolean;
        boolean data = true;

        // Test UpdateSettingRequest arg constructor
        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(settingType, componentSetting, data);
        assertEquals(componentSetting, updateSettingsRequest.getComponentSetting());
        assertEquals(settingType, updateSettingsRequest.getSettingType());
        assertEquals(data, updateSettingsRequest.getData());

        // Test UpdateSettingRequest StreamInput constructor
        try (BytesStreamOutput out = new BytesStreamOutput()) {
            updateSettingsRequest.writeTo(out);
            out.flush();
            try (BytesStreamInput in = new BytesStreamInput(BytesReference.toBytes(out.bytes()))) {
                updateSettingsRequest = new UpdateSettingsRequest(in);
                assertEquals(componentSetting, updateSettingsRequest.getComponentSetting());
                assertEquals(settingType, updateSettingsRequest.getSettingType());
                assertEquals(data, updateSettingsRequest.getData());
            }
        }

    }

    public void testRegisterHandler() throws Exception {
>>>>>>> main

        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);

        TransportService mockTransportService = spy(
            new TransportService(
                Settings.EMPTY,
                mock(Transport.class),
                null,
                TransportService.NOOP_TRANSPORT_INTERCEPTOR,
                x -> null,
                null,
                Collections.emptySet()
            )
        );
<<<<<<< HEAD

        extensionsManager.setTransportService(mockTransportService);
        verify(mockTransportService, times(3)).registerRequestHandler(anyString(), anyString(), anyBoolean(), anyBoolean(), any(), any());
=======
        extensionsManager.initializeServicesAndRestHandler(
            restController,
            settingsModule,
            mockTransportService,
            clusterService,
            settings,
            client
        );
        verify(mockTransportService, times(8)).registerRequestHandler(anyString(), anyString(), anyBoolean(), anyBoolean(), any(), any());
>>>>>>> main

    }

    public void testOnIndexModule() throws Exception {
<<<<<<< HEAD

        Path extensionDir = createTempDir();

        List<String> extensionsYmlLines = Arrays.asList(
            "extensions:",
            "   - name: firstExtension",
            "     uniqueId: uniqueid1",
            "     hostName: 'myIndependentPluginHost1'",
            "     hostAddress: '127.0.0.0'",
            "     port: '9300'",
            "     version: '0.0.7'",
            "     description: Fake description 1",
            "     opensearchVersion: '3.0.0'",
            "     javaVersion: '14'",
            "     className: fakeClass1",
            "     customFolderName: fakeFolder1",
            "     hasNativeController: false",
            "   - name: secondExtension",
            "     uniqueId: 'uniqueid2'",
            "     hostName: 'myIndependentPluginHost2'",
            "     hostAddress: '127.0.0.1'",
            "     port: '9301'",
            "     version: '3.14.16'",
            "     description: Fake description 2",
            "     opensearchVersion: '2.0.0'",
            "     javaVersion: '17'",
            "     className: fakeClass2",
            "     customFolderName: fakeFolder2",
            "     hasNativeController: true"
        );
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);

        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);

        transportService.start();
        transportService.acceptIncomingRequests();
        extensionsManager.setTransportService(transportService);
=======
        Files.write(extensionDir.resolve("extensions.yml"), extensionsYmlLines, StandardCharsets.UTF_8);
        ExtensionsManager extensionsManager = new ExtensionsManager(settings, extensionDir);
        initialize(extensionsManager);
>>>>>>> main

        Environment environment = TestEnvironment.newEnvironment(settings);
        AnalysisRegistry emptyAnalysisRegistry = new AnalysisRegistry(
            environment,
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap()
        );

        IndexSettings indexSettings = IndexSettingsModule.newIndexSettings("test_index", settings);
        IndexModule indexModule = new IndexModule(
            indexSettings,
            emptyAnalysisRegistry,
            new InternalEngineFactory(),
            new EngineConfigFactory(indexSettings),
            Collections.emptyMap(),
            () -> true,
            new IndexNameExpressionResolver(new ThreadContext(Settings.EMPTY)),
            Collections.emptyMap()
        );

        try (MockLogAppender mockLogAppender = MockLogAppender.createForLoggers(LogManager.getLogger(ExtensionsManager.class))) {

            mockLogAppender.addExpectation(
                new MockLogAppender.SeenEventExpectation(
                    "IndicesModuleRequest Failure",
                    "org.opensearch.extensions.ExtensionsManager",
                    Level.ERROR,
                    "IndicesModuleRequest failed"
                )
            );

            extensionsManager.onIndexModule(indexModule);
            mockLogAppender.assertAllExpectationsMatched();
        }
    }

<<<<<<< HEAD
=======
    private void initialize(ExtensionsManager extensionsManager) {
        transportService.start();
        transportService.acceptIncomingRequests();
        extensionsManager.initializeServicesAndRestHandler(
            restController,
            settingsModule,
            transportService,
            clusterService,
            settings,
            client
        );
    }
>>>>>>> main
}