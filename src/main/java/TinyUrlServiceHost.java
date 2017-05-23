/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

import java.util.logging.Level;

import com.vmware.xenon.common.CommandLineArgumentParser;
import com.vmware.xenon.services.common.RootNamespaceService;
import com.vmware.xenon.common.ServiceHost;

public class TinyUrlServiceHost extends ServiceHost {

    @Override
    public ServiceHost start() throws Throwable {
        super.start();
        startDefaultCoreServicesSynchronously();
        super.startService(new RootNamespaceService());
        super.startFactory(TinyUrlService.class, TinyUrlService::createFactory);
        super.startService(new TinyUrlGeneratorService());
        return this;
    }

    public static void main(String[] args) throws Throwable {
        Arguments arguments = new Arguments();
        CommandLineArgumentParser.parse(arguments, args);
        TinyUrlServiceHost host = new TinyUrlServiceHost();
        host.initialize(arguments);
        host.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            host.log(Level.WARNING, "Host stopping ...");
            host.stop();
            host.log(Level.WARNING, "Host is stopped");
        }));
    }
}
