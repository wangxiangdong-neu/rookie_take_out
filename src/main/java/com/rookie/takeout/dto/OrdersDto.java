package com.rookie.takeout.dto;

import com.rookie.takeout.entity.OrderDetail;
import com.rookie.takeout.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
	
}
