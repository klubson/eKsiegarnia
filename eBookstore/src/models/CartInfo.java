package models;

public class CartInfo {
    private int cartId;
    private int productCount;

    public CartInfo(int cartId) {
        this.cartId = cartId;
        this.productCount = 0;
    }

    public int getCartId() {
        return cartId;
    }

    public int getNextLP(){
        return productCount++;
    }
}
