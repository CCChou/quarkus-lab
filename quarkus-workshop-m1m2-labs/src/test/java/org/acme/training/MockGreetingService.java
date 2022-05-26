package org.acme.training;

import javax.enterprise.context.ApplicationScoped;

import org.acme.training.service.GreetingService;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped
public class MockGreetingService extends GreetingService {

    @Override
    public String greeting(String name) {
        return "hello " + name + " <<<<<<<<<< from mock greeting >>>>>>>>>>";
    }

}
