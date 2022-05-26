package org.acme.training.service;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.acme.training.model.EyeColor;
import org.acme.training.model.Person;
import org.eclipse.microprofile.opentracing.Traced;

import io.quarkus.vertx.ConsumeEvent;

@Traced
@ApplicationScoped
public class PersonService {

    @ConsumeEvent(value = "add-person", blocking = true)
    @Transactional
    public Person addPerson(String name) {
        LocalDate birth = LocalDate.now().plusWeeks(Math.round(Math.floor(Math.random() * 20 * 52 * -1)));
        EyeColor color = EyeColor.values()[(int) (Math.floor(Math.random() * EyeColor.values().length))];
        Person p = new Person();
        p.birth = birth;
        p.eyes = color;
        p.name = name;
        Person.persist(p);
        return p;
    }
}
