package de.demmer.dennis.autopost.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Log4j2
//@Component
public class SessionService implements ConnectInterceptor<Facebook> {

    private ConnectionRepository connectionRepository;
    private HttpSession session;

    @Autowired
    FacebookService facebookService;

    @Inject
    public SessionService(ConnectionRepository connectionRepository, HttpSession session) {
        this.connectionRepository = connectionRepository;
        this.session = session;
    }

    @Override
    public void preConnect(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> multiValueMap, WebRequest webRequest) {

    }

    @Override
    public void postConnect(Connection<Facebook> connection, WebRequest webRequest) {

        Facebook facebook = connection.getApi();

        UserOperations userOperations = facebook.userOperations();

        User user = userOperations.getUserProfile();

        log.info(user.getEmail());



    }
}
