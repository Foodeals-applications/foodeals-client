package net.foodeals.product.domain.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.foodeals.common.models.AbstractEntity;

@Entity 
@Table(name = "payment_methods_product")
@NoArgsConstructor
@Getter
public class PaymentMethodProduct  extends AbstractEntity<UUID>{

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String methodName; 
    
    
    public PaymentMethodProduct(String methodName) {
		this.methodName = methodName;
	}
    
    public PaymentMethodProduct(UUID id, String methodName) {
		this.id = id;
		this.methodName = methodName;
	}

    
    public static PaymentMethodProduct create(String methodName) {
		return new PaymentMethodProduct(methodName);
	}
    
	public static PaymentMethodProduct create(UUID id, String methodName) {
		return new PaymentMethodProduct(id,methodName);
	}
	
	public PaymentMethodProduct setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

   
}
