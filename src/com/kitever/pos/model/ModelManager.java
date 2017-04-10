package com.kitever.pos.model;

public class ModelManager {
	private static ModelManager modelManagerInstance;

	private AddUpdateDeleteModel addUpdateDeleteModelInstance;
	private PosCategoryModel fetchCategoryModelInstance;
	private ProductModel productModelInstance;
	private BrandModel fetchBrandModelInstance;
	private OrderDetailModel fetchOrderDetailModelInstance;
	private TaxModel taxModelInstance;
	private FetchParentCategoryModel fetchParentCategoryModelInstance;
	private FetchUnitModel fetchUnitModelInstance;
	private CreditModel creditModelInstance;
	private FetchProductModel fetchProductModelInstance;
	private FetchModeOfPaymentModel fetchModeOfPaymentModelInstance;
	private FetchContactModel fetchContactModelInstance;
	private FetchCustomerOrderDetailModel fetchCustomerOrderDetailModelInstance;

	private ModelManager() {
		// TODO Auto-generated constructor stub
	}

	public static ModelManager getInstance() {
		if (modelManagerInstance == null) {
			modelManagerInstance = new ModelManager();
		}
		return modelManagerInstance;
	}

	public void setAddUpdateDeleteModel(String response, String key) {
		addUpdateDeleteModelInstance = new AddUpdateDeleteModel(response, key);
	}

	public AddUpdateDeleteModel getAddUpdateDeleteModel() {
		return addUpdateDeleteModelInstance;
	}

	public void setCategoryModel(String response) {
		fetchCategoryModelInstance = new PosCategoryModel(response);
	}

	public PosCategoryModel getCategoryModel() {
		return fetchCategoryModelInstance;
	}

	public void setProductModel(String response) {
		productModelInstance = new ProductModel(response);
	}

	public ProductModel getProductModel() {
		return productModelInstance;
	}

	public void setBrandModel(String response) {
		fetchBrandModelInstance = new BrandModel(response);
	}

	public BrandModel getBrandModel() {
		return fetchBrandModelInstance;
	}

	public void setOrderDetailModel(String response) {
		fetchOrderDetailModelInstance = new OrderDetailModel(response);
	}

	public OrderDetailModel getOrderDetailModel() {
		return fetchOrderDetailModelInstance;
	}

	public void setTaxModel(String response) {
		taxModelInstance = new TaxModel(response);
	}

	public TaxModel getTaxModel() {
		return taxModelInstance;
	}

	public void setFetchParentCategoryModel(String response) {
		fetchParentCategoryModelInstance = new FetchParentCategoryModel(
				response);
	}

	public FetchParentCategoryModel getFetchParentCategoryModel() {
		return fetchParentCategoryModelInstance;
	}

	public void setFetchUnitModel(String response) {
		fetchUnitModelInstance = new FetchUnitModel(response);
	}

	public FetchUnitModel getFetchUnitModel() {
		return fetchUnitModelInstance;
	}

	public void setCreditModel(String response) {
		creditModelInstance = new CreditModel(response);
	}

	public CreditModel getCreditModel() {
		return creditModelInstance;
	}

	public void setFetchProductModel(String response) {
		fetchProductModelInstance = new FetchProductModel(response);
	}

	public FetchProductModel getFetchProductModel() {
		return fetchProductModelInstance;
	}

	public void setFetchModeOfPaymentModel(String response) {
		fetchModeOfPaymentModelInstance = new FetchModeOfPaymentModel(response);
	}

	public FetchModeOfPaymentModel getFetchModeOfPaymentModel() {
		return fetchModeOfPaymentModelInstance;
	}

	public void setFetchContactModel(String response) {
		fetchContactModelInstance = new FetchContactModel(response);
	}

	public FetchContactModel getFetchContactModel() {
		return fetchContactModelInstance;
	}
	public void setFetchCustomerOrderDetailModel(String response) {
		fetchCustomerOrderDetailModelInstance = new FetchCustomerOrderDetailModel(response);
	}

	public FetchCustomerOrderDetailModel getFetchCustomerOrderDetailModel() {
		return fetchCustomerOrderDetailModelInstance;
	}
}
