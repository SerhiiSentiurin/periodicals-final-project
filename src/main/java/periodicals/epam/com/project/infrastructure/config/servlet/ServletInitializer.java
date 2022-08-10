package periodicals.epam.com.project.infrastructure.config.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.*;
import lombok.extern.log4j.Log4j2;
import periodicals.epam.com.project.infrastructure.web.filter.encoding.EncodingFilter;
import periodicals.epam.com.project.infrastructure.web.filter.security.SecurityFilter;
import periodicals.epam.com.project.logic.controller.*;
import periodicals.epam.com.project.logic.dao.*;
import periodicals.epam.com.project.infrastructure.config.ConfigLoader;
import periodicals.epam.com.project.infrastructure.config.db.ConfigureDataSource;
import periodicals.epam.com.project.infrastructure.config.db.ConfigureLiquibase;
import periodicals.epam.com.project.infrastructure.web.*;
import periodicals.epam.com.project.infrastructure.web.exception.ExceptionHandler;
import periodicals.epam.com.project.logic.entity.UserRole;
import periodicals.epam.com.project.logic.services.*;


import javax.sql.DataSource;
import java.util.*;

@Log4j2
public class ServletInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) {
        servletContext.addListener(buildLocaleSessionListener());

        FilterRegistration.Dynamic security = servletContext.addFilter("security", new SecurityFilter());
        security.addMappingForUrlPatterns(null, false, "/*");
        FilterRegistration.Dynamic encoding = servletContext.addFilter("encoding", new EncodingFilter());
        encoding.addMappingForUrlPatterns(null, false, "/*");

        FrontServlet frontServlet = createFrontServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(frontServlet.getServletName(), frontServlet);
        dynamic.setLoadOnStartup(0);
        dynamic.addMapping("/periodicals/*");
        log.info("front servlet start up");
    }

    private LocaleSessionListener buildLocaleSessionListener() {
        List<Locale> locales = new ArrayList<>();
        Locale selectedLocale = new Locale("en");
        locales.add(selectedLocale);
        locales.add(new Locale("ua"));
        return new LocaleSessionListener(locales, selectedLocale);
    }

    private FrontServlet createFrontServlet() {
        DispatcherRequest dispatcherRequest = createDispatcherRequest();
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        ProcessorModelAndView processorModelAndView = new ProcessorModelAndView();
        return new FrontServlet("frontServlet", dispatcherRequest, exceptionHandler,
                processorModelAndView);
    }

    private DispatcherRequest createDispatcherRequest() {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.loadConfig("app.yaml");
        List<Placeholder> placeholders = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        QueryParameterHandler queryParameterHandler = new QueryParameterHandler(objectMapper);

        DataSource dataSource = new ConfigureDataSource().createDataSource(configLoader);
        ConfigureLiquibase liquibase = new ConfigureLiquibase(dataSource);
        liquibase.updateDatabase(configLoader);

        UserController userController = createUserController(queryParameterHandler, dataSource);
        placeholders.add(new Placeholder("POST", "login", userController::login));
        placeholders.add(new Placeholder("POST", "logout", userController::logout));
        placeholders.add(new Placeholder("POST", "changeLocale", userController::changeLocale));

        PeriodicalService periodicalService = getPeriodicalService(dataSource);
        ReaderService readerService = getReaderService(dataSource);
        AccountService accountService = getAccountService(dataSource);
        PrepaymentService prepaymentService = getPrepaymentService(dataSource);
        AdminService adminService = getAdminService(dataSource);

        PeriodicalController periodicalController = createPeriodicalController(periodicalService, readerService);
        ReaderController readerController = createReaderController(queryParameterHandler, periodicalService, readerService);
        AccountController accountController = createAccountController(queryParameterHandler, accountService);
        PrepaymentController prepaymentController = createPrepaymentController(queryParameterHandler, prepaymentService);
        AdminController adminController = createAdminController(queryParameterHandler, adminService);

        placeholders.add(new Placeholder("POST", "prepayment/addSubscription", prepaymentController::addSubscription));
        placeholders.add(new Placeholder("POST", "prepayment/deleteSubscription", prepaymentController::deleteSubscription));
        placeholders.add(new Placeholder("POST", "reader/create", readerController::createReader));
        placeholders.add(new Placeholder("GET", "reader", readerController::getReaderById));
        placeholders.add(new Placeholder("GET", "periodical/watch", periodicalController::getAllPeriodicals));
        placeholders.add(new Placeholder("GET", "periodical/watchByTopic", periodicalController::getPeriodicalsByTopic));
        placeholders.add(new Placeholder("GET", "periodical/findByName", periodicalController::getPeriodicalByName));
        placeholders.add(new Placeholder("GET", "periodical/sortByCost", periodicalController::sortPeriodicalsByCost));
        placeholders.add(new Placeholder("GET", "periodical/reversedSortByCost", periodicalController::reversedSortPeriodicalsByCost));
        placeholders.add(new Placeholder("GET", "periodical/sortByName", periodicalController::sortPeriodicalsByName));
        placeholders.add(new Placeholder("GET", "periodical/reversedSortByName", periodicalController::reversedSortPeriodicalsByName));
        placeholders.add(new Placeholder("GET", "periodical/periodicalsForSubscribing", periodicalController::getPeriodicalsForSubscribing));
        placeholders.add(new Placeholder("GET", "periodical/getByTopicPeriodicalsForSubscribing", periodicalController::getPeriodicalsForSubscribingByTopicByReaderId));
        placeholders.add(new Placeholder("GET", "periodical/findByNamePeriodicalsForSubscribing", periodicalController::findPeriodicalsForSubscribingByNameByReaderId));
        placeholders.add(new Placeholder("GET", "periodical/readerSubscriptions", periodicalController::getPeriodicalsByReaderId));
        placeholders.add(new Placeholder("GET", "periodical/getByTopicReaderSubscriptions", periodicalController::getPeriodicalsByTopicByReaderId));
        placeholders.add(new Placeholder("GET", "periodical/findByNameReaderSubscriptions", periodicalController::findPeriodicalsByNameByReaderId));
        placeholders.add(new Placeholder("POST", "account/topUpAccountAmount", accountController::topUpAccountAmount));
        placeholders.add(new Placeholder("GET", "account/getAccountInfo", accountController::getAmountOfMoneyByReaderId));
        placeholders.add(new Placeholder("GET", "admin/managePeriodicals", adminController::getAllPeriodicals));
        placeholders.add(new Placeholder("POST", "admin/createNewPeriodical", adminController::createNewPeriodical));
        placeholders.add(new Placeholder("POST", "admin/deletePeriodical", adminController::deletePeriodicalByPeriodicalId));
        placeholders.add(new Placeholder("POST","admin/deletePeriodicalForReaders", adminController::deletePeriodicalForReaders));
        placeholders.add(new Placeholder("POST", "admin/restorePeriodicalForReaders", adminController::restorePeriodicalForReaders));
        placeholders.add(new Placeholder("GET", "admin/getPeriodicalForEdit", adminController::getPeriodicalById));
        placeholders.add(new Placeholder("POST", "admin/editPeriodical", adminController::editPeriodicalById));
        placeholders.add(new Placeholder("GET", "admin/manageReaders", adminController::getAllReaders));
        placeholders.add(new Placeholder("POST", "admin/lockReader", adminController::lockReader));
        placeholders.add(new Placeholder("POST", "admin/unlockReader", adminController::unlockReader));
        return new DispatcherRequest(placeholders);
    }

    private UserController createUserController(QueryParameterHandler queryParameterHandler, DataSource dataSource) {
        Map<UserRole, String> mapView = Map.of(UserRole.ADMIN, "/admin/adminHome.jsp", UserRole.READER, "/reader/readerHome.jsp");
        UserDAO userDao = new UserDAO(dataSource);
        UserService userService = new UserService(userDao);
        return new UserController(userService, queryParameterHandler, mapView);
    }

    private ReaderController createReaderController(QueryParameterHandler queryParameterHandler, PeriodicalService periodicalService, ReaderService readerService) {
        return new ReaderController(readerService, periodicalService, queryParameterHandler);
    }

    private PeriodicalController createPeriodicalController(PeriodicalService periodicalService, ReaderService readerService) {
        return new PeriodicalController(periodicalService, readerService);
    }

    private AccountController createAccountController(QueryParameterHandler queryParameterHandler, AccountService accountService) {
        return new AccountController(accountService, queryParameterHandler);
    }

    private PrepaymentController createPrepaymentController(QueryParameterHandler queryParameterHandler, PrepaymentService prepaymentService) {
        return new PrepaymentController(prepaymentService, queryParameterHandler);
    }

    private AdminController createAdminController(QueryParameterHandler queryParameterHandler, AdminService adminService) {
        return new AdminController(adminService, queryParameterHandler);
    }

    private PrepaymentService getPrepaymentService(DataSource dataSource) {
        PrepaymentDAO prepaymentDAO = new PrepaymentDAO(dataSource);
        return new PrepaymentService(prepaymentDAO);
    }

    private AccountService getAccountService(DataSource dataSource) {
        AccountDAO accountDAO = new AccountDAO(dataSource);
        return new AccountService(accountDAO);
    }

    private PeriodicalService getPeriodicalService(DataSource dataSource) {
        PeriodicalDAO periodicalDAO = new PeriodicalDAO(dataSource);
        return new PeriodicalService(periodicalDAO);
    }

    private ReaderService getReaderService(DataSource dataSource) {
        ReaderDAO readerDAO = new ReaderDAO(dataSource);
        return new ReaderService(readerDAO);
    }

    private AdminService getAdminService(DataSource dataSource) {
        AdminDAO adminDAO = new AdminDAO(dataSource);
        return new AdminService(adminDAO);
    }
}
