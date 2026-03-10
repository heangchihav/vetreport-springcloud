# Branch Report Service

A Spring Boot microservice for managing products and product types with full CRUD operations. This service is designed to handle box selling scenarios where products have multiple types (sizes) with different prices.

## Features

### Product Management
- Create, Read, Update, Delete products
- Product categories and SKU management
- Active/inactive status tracking
- Search and filtering capabilities
- Pagination support

### Product Type Management
- Multiple types per product (e.g., Size A, Size B)
- Individual pricing per type
- Stock management with low stock alerts
- Cost price tracking
- Weight and dimensions support
- Size code management

### Advanced Features
- Low stock monitoring
- Price range filtering
- Comprehensive search functionality
- Validation and error handling
- RESTful API design
- PostgreSQL database integration

## API Endpoints

### Products
- `GET /api/products` - Get all products (paginated)
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `PATCH /api/products/{id}/deactivate` - Deactivate product
- `GET /api/products/search` - Search products
- `GET /api/products/category/{category}` - Get products by category
- `GET /api/products/categories` - Get all active categories

### Product Types
- `GET /api/product-types` - Get all product types (paginated)
- `GET /api/product-types/{id}` - Get product type by ID
- `POST /api/product-types?productId={id}` - Create new product type
- `PUT /api/product-types/{id}` - Update product type
- `DELETE /api/product-types/{id}` - Delete product type
- `PATCH /api/product-types/{id}/deactivate` - Deactivate product type
- `GET /api/product-types/product/{productId}` - Get types by product
- `GET /api/product-types/product/{productId}/active` - Get active types by product
- `GET /api/product-types/product/{productId}/search` - Search product types
- `GET /api/product-types/price-range` - Get types by price range
- `GET /api/product-types/low-stock` - Get low stock items
- `PATCH /api/product-types/{id}/stock` - Update stock quantity
- `PATCH /api/product-types/{id}/stock/adjust` - Adjust stock quantity

### System
- `GET /api/health` - Health check
- `GET /api/info` - Service information

## Database Schema

### Products Table
- `id` - Primary key
- `name` - Product name (required)
- `description` - Product description
- `category` - Product category (required)
- `sku` - Unique SKU
- `active` - Active status
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

### Product Types Table
- `id` - Primary key
- `name` - Type name (required)
- `size_code` - Size identifier
- `dimensions` - Physical dimensions
- `price` - Selling price (required, > 0)
- `cost_price` - Cost price
- `weight` - Weight
- `stock_quantity` - Current stock
- `min_stock_level` - Minimum stock threshold
- `active` - Active status
- `product_id` - Foreign key to products
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

## Configuration

### Application Properties
- Server port: 8083
- Database: PostgreSQL
- JPA/Hibernate with DDL auto-update
- Actuator endpoints enabled

### Database Setup
```sql
CREATE DATABASE branchreport_db;
```

## Running the Service

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### Docker Support
```bash
docker build -t branchreport-service .
docker run -p 8083:8083 branchreport-service
```

## Usage Examples

### Create a Product
```json
POST /api/products
{
  "name": "Shipping Box",
  "description": "Standard shipping box",
  "category": "Packaging",
  "sku": "BOX-001",
  "active": true,
  "productTypes": [
    {
      "name": "Small",
      "sizeCode": "S",
      "dimensions": "10x8x6 inches",
      "price": 5.99,
      "costPrice": 3.50,
      "stockQuantity": 100,
      "minStockLevel": 10
    },
    {
      "name": "Medium",
      "sizeCode": "M", 
      "dimensions": "15x12x10 inches",
      "price": 9.99,
      "costPrice": 6.00,
      "stockQuantity": 50,
      "minStockLevel": 5
    }
  ]
}
```

### Update Stock
```bash
PATCH /api/product-types/1/stock?quantity=150
```

### Adjust Stock
```bash
PATCH /api/product-types/1/stock/adjust?adjustment=-10
```

## Error Handling

The service provides comprehensive error handling with:
- Validation errors (400)
- Not found errors (404)
- Conflict errors (409)
- Internal server errors (500)

All errors include detailed messages and timestamps.

## Monitoring

- Health check endpoint: `/api/health`
- Actuator metrics: `/actuator/metrics`
- Prometheus metrics: `/actuator/prometheus`
