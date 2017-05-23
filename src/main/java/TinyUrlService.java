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

import java.net.MalformedURLException;
import java.net.URL;

import com.vmware.xenon.common.FactoryService;
import com.vmware.xenon.common.Operation;
import com.vmware.xenon.common.ServiceDocument;
import com.vmware.xenon.common.ServiceDocumentDescription.PropertyUsageOption;
import com.vmware.xenon.common.StatefulService;

public class TinyUrlService extends StatefulService {
    public static final String FACTORY_LINK = "tinyurl";

    public static FactoryService createFactory() {
        return FactoryService.createIdempotent(TinyUrlService.class);
    }

    public static class UrlState extends ServiceDocument {
        @UsageOption(option = PropertyUsageOption.REQUIRED)
        public String url;
    }

    public TinyUrlService() {
        super(UrlState.class);
        super.toggleOption(ServiceOption.PERSISTENCE, true);
        super.toggleOption(ServiceOption.REPLICATION, true);
        super.toggleOption(ServiceOption.OWNER_SELECTION, true);
    }

    @Override
    public void handleStart(Operation op) {
        if (!op.hasBody()) {
            op.fail(new IllegalArgumentException("body is required"));
            return;
        }

        UrlState state = op.getBody(UrlState.class);
        if (!validate(op, state)) {
            return;
        }

        op.complete();
    }

    private boolean validate(Operation op, UrlState state) {
        if (state.url == null) {
            op.fail(new IllegalArgumentException("url is required"));
            return false;
        }

        try {
            new URL(state.url);
        } catch (MalformedURLException e) {
            // the URL is not in a valid form
            return false;
        }

        return true;
    }
}
