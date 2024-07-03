package JAVAC5.com.WebOrderDoAn.Services;

import JAVAC5.com.WebOrderDoAn.Entities.Invoice;
import JAVAC5.com.WebOrderDoAn.Entities.InvoiceItem;
import JAVAC5.com.WebOrderDoAn.Repositories.IFoodRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IInvoiceRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IInvoiceItemRepository;
import JAVAC5.com.WebOrderDoAn.Entities.Cart;
import JAVAC5.com.WebOrderDoAn.Entities.CartItem;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})

public class CartService {
    @Autowired
    private static final String CART_SESSION_KEY = "cart";
    private final IInvoiceRepository invoiceRepository;
    private final IInvoiceItemRepository itemInvoiceRepository;
    private final IFoodRepository iFoodRepository;
    public Cart getCart(@NotNull HttpSession session) {
        return Optional.ofNullable((Cart)
                        session.getAttribute(CART_SESSION_KEY))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    session.setAttribute(CART_SESSION_KEY, cart);
                    return cart;
                });
    }
    public void updateCart(@NotNull HttpSession session, Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }
    public void removeCart(@NotNull HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
    public int getSumQuantity(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    public double getSumPrice(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToDouble(item -> item.getPrice() *
                        item.getQuantity())
                .sum();
    }
    public void saveCart(@NotNull HttpSession session) {
        var cart = getCart(session);
        if (cart.getCartItems().isEmpty()) return;
        var invoice = new Invoice();
        invoice.setInvoiceDate(new Date(new Date().getTime()));
        invoice.setPrice(getSumPrice(session));
        invoiceRepository.save(invoice);
        cart.getCartItems().forEach(item -> {
            var items = new InvoiceItem();
            items.setInvoice(invoice);
            items.setQuantity(item.getQuantity());
            items.setFood(iFoodRepository.findById(item.getFoodId())
                    .orElseThrow());
            itemInvoiceRepository.save(items);
        });
        removeCart(session);
    }

}