package com.example.a10spring_boot_hibernate_library.services;

import com.example.a10spring_boot_hibernate_library.entities.ClientOrder;
import com.example.a10spring_boot_hibernate_library.entities.OrderItem;
import com.example.a10spring_boot_hibernate_library.repository.ClientOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientOrderService {

    private ClientOrderRepository clientOrderRepository;

    private OrderItemService orderItemService;
    /*@Autowired
    private PaymentService paymentService;*/

    @Autowired //pas besoin de faire un new
    public ClientOrderService(ClientOrderRepository clientOrderRepository, OrderItemService orderItemService) {
        this.clientOrderRepository = clientOrderRepository;
        this.orderItemService = orderItemService;
    }

    public List<ClientOrder> findall() {
        return clientOrderRepository.findAll();
    }


    public boolean deleteClientOrderById(int id) {
        ClientOrder tempClientOrder = this.findClientOrderById(id);
        if (tempClientOrder != null) {
            //effacer tous la liste de itemOrders du client
            List<OrderItem> liste = (List<OrderItem>) tempClientOrder.getOrderItemsByOrderId();
            if (liste != null) {
                for (OrderItem orderItem: liste){
                    orderItemService.deleteOrderItemById(orderItem.getId());
                }
                tempClientOrder.setOrderItemsByOrderId(null);
            }
            //effacer le payment du client
            /*Payment payment = tempClientOrder.getPayment();
            if (payment != null) {
                paymentService.deletePaymentById(payment.getPaymentId());
                tempClientOrder.setPayment(null);
            }*/
            //mettre le client a null
            tempClientOrder.setClientByClientId(null);

            clientOrderRepository.save(tempClientOrder);
            clientOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ClientOrder findClientOrderById(int id) {
        return clientOrderRepository.findById(id).get();
    }
}
