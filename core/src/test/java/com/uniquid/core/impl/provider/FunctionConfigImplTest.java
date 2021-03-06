/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.core.impl.provider;

import com.uniquid.core.provider.FunctionContext;
import com.uniquid.core.provider.impl.FunctionConfigImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Enumeration;

public class FunctionConfigImplTest {

    @Test
    public void testFunctionConfig() {

        FunctionContext functionContext = new FunctionContext() {

            @Override
            public void setAttribute(String name, Object object) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeAttribute(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public String getServerInfo() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object getAttribute(String name) {
                // TODO Auto-generated method stub
                return null;
            }
        };

        FunctionConfigImpl functionConfigImpl = new FunctionConfigImpl(functionContext);

        Assert.assertEquals(functionContext, functionConfigImpl.getFunctionContext());

    }

}
