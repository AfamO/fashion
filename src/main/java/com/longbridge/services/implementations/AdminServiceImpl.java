package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.AdminDashBoardDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Designer;
import com.longbridge.models.Orders;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.repository.DesignerRepository;
import com.longbridge.repository.OrderRepository;
import com.longbridge.repository.PaymentRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Longbridge on 08/08/2018.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    GeneralUtil generalUtil;
    @Autowired
    PaymentRepository paymentRepository;


    @Override
    public AdminDashBoardDTO getDashboardData() {
        try {
            AdminDashBoardDTO adminDashBoardDTO = new AdminDashBoardDTO();
            Long totalProducts = productRepository.countByVerifiedFlag("Y");
            Long totalOrders = (long) orderRepository.findAll().size();
            List<Designer> designers = designerRepository.findTop10ByOrderByCreatedOnDesc();
            List<Orders> orders = orderRepository.findByDeliveryStatusNot("NV");
            adminDashBoardDTO.setTotalProducts(totalProducts);
            adminDashBoardDTO.setTotalOrders(totalOrders);
            adminDashBoardDTO.setRecentCustomers(generalUtil.convDesignerEntToDTOs(designers));
            adminDashBoardDTO.setRecentOrders(generalUtil.convertOrderEntsToDTOs(orders));
            adminDashBoardDTO.setNewOrders(orderRepository.NoOfOrdersByStatus("P")) ; //get the number of pending orders as new  new orders
            adminDashBoardDTO.setTotalSales(orderRepository.NoOfOrdersByStatus("PC"));// get the total  number of sold orders
            adminDashBoardDTO.setTotalPayment(paymentRepository.getTotalPayment());//  get the total payment of all orders sold.
            return adminDashBoardDTO;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }
}
