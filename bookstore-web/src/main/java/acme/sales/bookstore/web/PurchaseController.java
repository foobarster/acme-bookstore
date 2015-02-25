package acme.sales.bookstore.web;

import acme.sales.bookstore.domain.repositories.BookRepository;
import acme.sales.bookstore.domain.repositories.ClientRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author vmuravlev
 */
@Controller
@Scope("request")
@RequestMapping("/purchase")
public class PurchaseController {

    @Inject
    private Cart cart;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private ClientRepository clientRepository;

    @RequestMapping("/newPurchase.action")
    public ModelAndView newPurchase() {
        cart.clear();
        return selectBooks();
    }

    @RequestMapping(value = "/selectBooks.action", method = RequestMethod.GET)
    public ModelAndView selectBooks() {
        return new ModelAndView("selectBooks", "allBooks", bookRepository.findAll());
    }

    @RequestMapping(value = "/addToCart.action", method = RequestMethod.POST)
    public ModelAndView addToCart(@RequestParam Map<String, String> lines) {
        cart.addLines(lines);
        ModelAndView modelAndView = selectBooks();
        modelAndView.addObject("message", "Books were successfully added");
        return modelAndView;
    }

    @RequestMapping(value = "/showCart.action")
    public String showCart() {
        return "showCart";
    }

    @RequestMapping("/selectClient.action")
    public ModelAndView selectClient() {
        return new ModelAndView("selectClient", "allClients", clientRepository.findAll());
    }
}