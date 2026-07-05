export type ProductType = 'RETIREMENT' | 'SAVINGS' | 'TAX_FREE';

export interface Product {
  id: number;
  name: string;
  type: ProductType;
  currentBalance: number;
}

export interface Portfolio {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: string;
  age: number;
  products: Product[];
}

export interface WithdrawalNotice {
  id: number;
  investorId: number;
  investorName: string;
  productId: number;
  productName: string;
  productType: ProductType;
  amount: number;
  balanceBefore: number;
  balanceAfter: number;
  createdAt: string;
  status: 'CREATED' | 'REJECTED';
}

export interface ApiError {
  message: string;
  validationErrors?: Record<string, string>;
}
