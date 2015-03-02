package acme.sales.bookstore.web;

import acme.sales.bookstore.domain.entities.Client;
import acme.sales.bookstore.domain.services.AdministrationException;
import acme.sales.bookstore.domain.services.AdministrationService;
import acme.sales.bookstore.domain.services.DashboardService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author vmuravlev
 */
@Controller
@Scope("request")
public class AdministrationController {

    @Inject
    private AdministrationService administrationService;

    @Inject
    private DashboardService dashboardService;

    @RequestMapping("/signUp.action")
    public ModelAndView prepareNewClient() {
        return new ModelAndView("userProfile", "client", new Client());
    }

    @RequestMapping("/createNewClient.action")
    public ModelAndView createNewClient(Client client) {
        List<String> errors = validate(client);

        if (!errors.isEmpty()) {
            return new ModelAndView("userProfile", "errors", errors);
        }

        try {
            administrationService.createNewClient(client);
            return new ModelAndView("login");
        } catch (AdministrationException e) {
            return new ModelAndView("userProfile", "errors", e.getMessage());
        }
    }

    @RequestMapping("/manager/showDashboard.action")
    public ModelAndView showDashboard(Date date) {
        Date statsDate = date == null ? new Date() : date;
        return new ModelAndView("dashboard", "stats", dashboardService.getStats(statsDate));
    }

    private List<String> validate(Client client) {
        List<String> errors = new ArrayList<>();

        if (client.getFirstName() == null || "".equals(client.getFirstName())) {
            errors.add("First name is required");
        }

        if (client.getLastName() == null || "".equals(client.getLastName())) {
            errors.add("Last name is required");
        }
        return errors;
    }
}