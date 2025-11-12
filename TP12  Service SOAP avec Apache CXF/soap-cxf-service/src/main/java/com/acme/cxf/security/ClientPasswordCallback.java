package com.acme.cxf.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import java.io.IOException;

public class ClientPasswordCallback implements CallbackHandler {
    private final String username;
    private final String password;

    public ClientPasswordCallback(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback cb : callbacks) {
            if (cb instanceof WSPasswordCallback pc) {
                pc.setPassword(password);
                pc.setIdentifier(username);
            }
        }
    }
}
