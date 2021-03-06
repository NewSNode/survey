package org.survey.service.poll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.survey.config.ServiceTestConfig;
import org.survey.model.poll.Poll;
import org.survey.model.poll.PollComparator;
import org.survey.model.poll.PollFactory;
import org.survey.model.user.User;
import org.survey.model.user.UserFactory;
import org.survey.service.user.UserService;

/**
 * Test UserService. Must use SpringJUnit4ClassRunner to enable spring
 * injection. Loaded Spring configuration is defined by ContextConfiguration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy(@ContextConfiguration(classes = ServiceTestConfig.class))
public class PollServiceTestBase {
    protected static int ENTITY_COUNT = 2;
    protected List<Poll> orginalEntities = new ArrayList<Poll>();
    protected List<Poll> savedEntities = new ArrayList<Poll>();
    @Resource(name="pollService")
    protected PollService entityService;
    protected PollFactory entityFactory;
    protected PollComparator entityComparator = new PollComparator();
    @Resource
    protected UserService userService;
    private User user;

    @Before
    public void setUp() {
        UserFactory userFactory = new UserFactory();
        user = userFactory.getEntities(1).get(0);
//        user = userRepository.save(user);
        user = userService.create(user);
        entityFactory = new PollFactory(user);
    }

    @After
    public void tearDown() throws SQLException {
        Poll[] users = entityService.findAll();
        if (users != null) {
            for (Poll pollToDelete : users) {
                entityService.delete(pollToDelete.getId());
            }
        }
        userService.delete(user.getId());
    }

    @Test
    public void create() {
        orginalEntities = entityFactory.getEntities(ENTITY_COUNT);
        for (int i = 0; i < ENTITY_COUNT; i++) {
            Poll originalEntity = orginalEntities.get(i);
            Poll savedEntity = entityService.create(originalEntity);
            savedEntities.add(savedEntity);
            assertEntity(originalEntity, savedEntity);
        }
    }

    @Test
    public void createWithError() {
        try {
            create();
            entityService.create(orginalEntities.get(0));
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage()
                    .startsWith("Poll already exists: "));
        }
    }

    @Test
    public void update() {
        create();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            Poll foundEntity = entityService.findById(savedEntities.get(i)
                    .getId());
            Poll updatedEntity = entityFactory.getUpdatedEntity(foundEntity);
            updatedEntity.setId(foundEntity.getId());
            entityService.update(updatedEntity);
            foundEntity = entityService.findById(savedEntities.get(i).getId());
            assertEntity(updatedEntity, foundEntity);
        }
    }

    @Test
    public void findAll() {
        create();
        Assert.assertEquals(ENTITY_COUNT, entityService.findAll().length);
    }

    @Test
    public void findByOwner() {
        create();
        Assert.assertEquals(
                ENTITY_COUNT,
                entityService.findByOwner(savedEntities.get(0).getOwner()
                        .getUsername()).length);
    }

    @Test
    public void findOne() {
        create();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            Poll originalEntity = orginalEntities.get(i);
            Poll foundEntity = entityService.findById(savedEntities.get(i).getId());
            assertEntity(originalEntity, foundEntity);
        }
        // TODO how to test a non-existent entity?
    }

    @Test
    public void exists() {
        create();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            entityService.exists(savedEntities.get(i).getId());
        }
        // TODO how to test if exists fails?
        // Assert.assertFalse(entityRepository.exists((ID) new Object()));
    }

    @Test
    public void existsWithNull() {
        // try with non-existent id
        Assert.assertFalse(entityService.exists(666L));
    }

    @Test
    public void count() {
        create();
        Assert.assertEquals(ENTITY_COUNT, entityService.count());
    }

    @Test
    public void delete() {
        create();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            Poll entity = savedEntities.get(i);
            entityService.delete(entity.getId());
            Assert.assertFalse(entityService.exists(entity.getId()));
        }
        Assert.assertEquals(0, entityService.count());
    }

    @Test
    public void deleteNonexistent() {
        create();
        entityService.delete(666L);
    }

    public void assertEntity(Poll originalEntity, Poll entity) {
        Assert.assertEquals("originalEntity: " + originalEntity + " entity: "
                + entity, 0, entityComparator.compare(originalEntity, entity));
        Assert.assertNotNull(originalEntity.getQuestions());
        Assert.assertNotNull(entity.getQuestions());
        Assert.assertEquals(originalEntity.getQuestions().size(),
                entity.getQuestions().size());
        Assert.assertEquals(originalEntity.getQuestions().get(0).getType(),
                entity.getQuestions().get(0).getType());
    }
}
