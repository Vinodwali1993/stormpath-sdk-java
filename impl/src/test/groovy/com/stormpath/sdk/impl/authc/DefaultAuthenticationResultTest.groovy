package com.stormpath.sdk.impl.authc

import com.stormpath.sdk.api.ApiAuthenticationResult
import com.stormpath.sdk.authc.AuthenticationResult
import com.stormpath.sdk.authc.AuthenticationResultVisitor
import com.stormpath.sdk.impl.ds.InternalDataStore
import com.stormpath.sdk.oauth.AccessTokenResult
import com.stormpath.sdk.oauth.OauthAuthenticationResult
import org.testng.annotations.Test

import static junit.framework.TestCase.assertNotNull
import static org.easymock.EasyMock.createStrictMock

class DefaultAuthenticationResultTest {

    @Test
    void testGetPropertyDescriptors() {

        def internalDataStore = createStrictMock(InternalDataStore)
        def authenticationResult = new DefaultAuthenticationResult(internalDataStore)

        def propertyDescriptors = authenticationResult.getPropertyDescriptors()

        assertNotNull propertyDescriptors.get(DefaultAuthenticationResult.ACCOUNT.name)
    }

    @Test
    void testAccept() {

        def internalDataStore = createStrictMock(InternalDataStore)
        def authenticationResult = new DefaultAuthenticationResult(internalDataStore)

        authenticationResult.accept(new AuthenticationResultVisitor() {
            @Override
            void visit(AuthenticationResult result) {

                assertNotNull result
            }

            @Override
            void visit(ApiAuthenticationResult result) {

                fail("shouldn't be here")
            }

            @Override
            void visit(OauthAuthenticationResult result) {

            }

            @Override
            void visit(AccessTokenResult result) {

                fail("shouldn't be here")
            }
        })
    }
}
