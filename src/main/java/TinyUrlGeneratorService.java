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
import java.util.Random;

import com.vmware.xenon.common.Operation;
import com.vmware.xenon.common.StatelessService;

public class TinyUrlGeneratorService extends StatelessService {
    public static final String SELF_LINK = "tinyurlgenerator";

    // curl localhost:8000/tinyurlgenerator
    @Override
    public void handleGet(Operation get) {
        get.setBody(generateAlias());
        get.complete();
    }

    // Settings for building Tiny Alias
    private final String ALIAS_CHAR_SET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private final int ALIAS_URL_LEN = 6;

    private String generateAlias() {
        String alias = "";
        Random random = new Random();
        for (int i = 0; i < ALIAS_URL_LEN; ++i) {
            alias += ALIAS_CHAR_SET.charAt(random.nextInt(ALIAS_CHAR_SET.length()));
        }

        return alias;
    }
}
