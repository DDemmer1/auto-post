package de.demmer.dennis.autopost.interceptors;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

public class FacebookConnectInterceptor implements ConnectInterceptor<Facebook> {


    private ConnectionRepository connectionRepository;
    private HttpSession session;


    @Inject
    public FacebookConnectInterceptor(ConnectionRepository connectionRepository, HttpSession session) {
        this.connectionRepository = connectionRepository;
        this.session = session;
    }

    @Override
    public void preConnect(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> multiValueMap, WebRequest webRequest) {

    }

    @Override
    public void postConnect(Connection<Facebook> connection, WebRequest webRequest) {

    }
}
