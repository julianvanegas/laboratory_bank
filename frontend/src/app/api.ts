export type Customer = {
  id: number;
  firstName: string;
  lastName: string;
  accountNumber: string;
  balance: number;
};

export type Transaction = {
  id?: number;
  senderAccountNumber: string;
  receiverAccountNumber: string;
  amount: number;
  timestamp?: string;
};

const API_BASE = "/api/backend";

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: { "Content-Type": "application/json", ...options?.headers },
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `La solicitud falló (${response.status})`);
  }

  return response.json() as Promise<T>;
}

export const bankApi = {
  getCustomers: () => request<Customer[]>("/customers"),
  getCustomer: (id: number) => request<Customer>(`/customers/${id}`),
  createCustomer: (customer: Omit<Customer, "id">) =>
    request<Customer>("/customers", {
      method: "POST",
      body: JSON.stringify(customer),
    }),
  transfer: (transaction: Omit<Transaction, "id" | "timestamp">) =>
    request<Transaction>("/transactions", {
      method: "POST",
      body: JSON.stringify(transaction),
    }),
  getTransactions: (accountNumber: string) =>
    request<Transaction[]>(`/transactions/${encodeURIComponent(accountNumber)}`),
};
