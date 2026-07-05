import {
  Alert,
  Box,
  Button,
  Chip,
  Container,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Snackbar,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Tooltip,
  Typography
} from '@mui/material';
import { AxiosError } from 'axios';
import { Download, RefreshCw, Send } from 'lucide-react';
import { useEffect, useMemo, useState } from 'react';
import { createWithdrawal, getPortfolio, getWithdrawals, statementUrl } from './api';
import type { ApiError, Portfolio, Product, WithdrawalNotice } from './types';

const investorOptions = [
  { id: 1, label: 'Grace Mokoena' },
  { id: 2, label: 'Thabo Dlamini' }
];

function money(value: number) {
  return new Intl.NumberFormat('en-ZA', { style: 'currency', currency: 'ZAR' }).format(value);
}

function formatDateForInput(dateString: string): string {
  if (!dateString) return '';
  const [year, month, day] = dateString.split('-');
  return `${day}/${month}/${year}`;
}

function errorMessage(error: unknown) {
  const axiosError = error as AxiosError<ApiError>;
  return axiosError.response?.data?.message ?? 'Something went wrong. Please try again.';
}

export default function App() {
  const [investorId, setInvestorId] = useState(1);
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);
  const [withdrawals, setWithdrawals] = useState<WithdrawalNotice[]>([]);
  const [productId, setProductId] = useState('');
  const [amount, setAmount] = useState('');
  const [from, setFrom] = useState('');
  const [to, setTo] = useState('');
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const selectedProduct = useMemo(
    () => portfolio?.products.find((product) => product.id === Number(productId)),
    [portfolio, productId]
  );

  const formError = useMemo(() => {
    if (!selectedProduct || !amount) return '';
    const numericAmount = Number(amount);
    if (Number.isNaN(numericAmount) || numericAmount <= 0) return 'Enter an amount greater than zero.';
    if (numericAmount > selectedProduct.currentBalance) return 'Withdrawal cannot exceed the current balance.';
    if (numericAmount > selectedProduct.currentBalance * 0.9) return 'Withdrawal cannot exceed 90% of the balance.';
    if (selectedProduct.type === 'RETIREMENT' && portfolio && portfolio.age <= 65) {
      return 'Retirement withdrawals require an investor older than 65.';
    }
    return '';
  }, [amount, portfolio, selectedProduct]);

  async function load() {
    setLoading(true);
    try {
      const [nextPortfolio, nextWithdrawals] = await Promise.all([
        getPortfolio(investorId),
        getWithdrawals(investorId)
      ]);
      setPortfolio(nextPortfolio);
      setWithdrawals(nextWithdrawals);
      setProductId(String(nextPortfolio.products[0]?.id ?? ''));
    } catch (error) {
      setNotice({ type: 'error', text: errorMessage(error) });
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [investorId]);

  async function submitWithdrawal() {
    if (!selectedProduct || formError) return;
    try {
      await createWithdrawal(investorId, selectedProduct.id, Number(amount));
      setAmount('');
      setNotice({ type: 'success', text: 'Withdrawal notice created.' });
      await load();
    } catch (error) {
      setNotice({ type: 'error', text: errorMessage(error) });
    }
  }

  const totalBalance = portfolio?.products.reduce((sum, product) => sum + product.currentBalance, 0) ?? 0;

  return (
    <Box className="app-shell">
      <Container maxWidth="xl" sx={{ py: 4 }}>
        <Stack direction={{ xs: 'column', md: 'row' }} justifyContent="space-between" spacing={2} sx={{ mb: 3 }}>
          <Box>
            <Typography variant="h1">Enviro365 Portfolio</Typography>
            <Typography color="text.secondary">Investor dashboard and withdrawal notices</Typography>
          </Box>
          <Stack direction="row" spacing={1} alignItems="center">
            <FormControl size="small" sx={{ minWidth: 220 }}>
              <InputLabel>Investor</InputLabel>
              <Select
                label="Investor"
                value={investorId}
                onChange={(event) => setInvestorId(Number(event.target.value))}
              >
                {investorOptions.map((option) => (
                  <MenuItem key={option.id} value={option.id}>{option.label}</MenuItem>
                ))}
              </Select>
            </FormControl>
            <Tooltip title="Reload portfolio">
              <Button variant="outlined" startIcon={<RefreshCw size={18} />} onClick={load} disabled={loading}>
                Refresh
              </Button>
            </Tooltip>
          </Stack>
        </Stack>

        <Grid container spacing={2}>
          <Grid item xs={12} md={8}>
            <Paper variant="outlined" sx={{ p: 2 }}>
              <Stack direction={{ xs: 'column', sm: 'row' }} justifyContent="space-between" spacing={2} sx={{ mb: 2 }}>
                <Box>
                  <Typography variant="h2">
                    {portfolio ? `${portfolio.firstName} ${portfolio.lastName}` : 'Loading portfolio'}
                  </Typography>
                  <Typography color="text.secondary">
                    {portfolio ? `${portfolio.email} · Age ${portfolio.age}` : ''}
                  </Typography>
                </Box>
                <Box>
                  <Typography color="text.secondary" align="right">Total balance</Typography>
                  <Typography variant="h2" className="money">{money(totalBalance)}</Typography>
                </Box>
              </Stack>
              <Grid container spacing={2}>
                {portfolio?.products.map((product) => (
                  <Grid item xs={12} sm={6} key={product.id}>
                    <Paper variant="outlined" sx={{ p: 2, height: '100%' }}>
                      <Stack spacing={1}>
                        <Stack direction="row" justifyContent="space-between" alignItems="center">
                          <Typography fontWeight={700}>{product.name}</Typography>
                          <Chip size="small" label={product.type.replace('_', ' ')} />
                        </Stack>
                        <Typography variant="h2" className="money">{money(product.currentBalance)}</Typography>
                      </Stack>
                    </Paper>
                  </Grid>
                ))}
              </Grid>
            </Paper>
          </Grid>

          <Grid item xs={12} md={4}>
            <Paper variant="outlined" sx={{ p: 2 }}>
              <Typography variant="h2" sx={{ mb: 2 }}>Withdrawal Notice</Typography>
              <Stack spacing={2}>
                <FormControl fullWidth size="small">
                  <InputLabel>Product</InputLabel>
                  <Select label="Product" value={productId} onChange={(event) => setProductId(event.target.value)}>
                    {portfolio?.products.map((product: Product) => (
                      <MenuItem key={product.id} value={String(product.id)}>
                        {product.name}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <TextField
                  size="small"
                  label="Amount"
                  type="number"
                  value={amount}
                  onChange={(event) => setAmount(event.target.value)}
                  error={Boolean(formError)}
                  helperText={formError || 'Maximum allowed is 90% of the product balance.'}
                  inputProps={{ min: 0, step: '0.01' }}
                />
                <Button
                  variant="contained"
                  startIcon={<Send size={18} />}
                  onClick={submitWithdrawal}
                  disabled={!selectedProduct || !amount || Boolean(formError)}
                >
                  Create notice
                </Button>
              </Stack>
            </Paper>
          </Grid>

          <Grid item xs={12}>
            <Paper variant="outlined" sx={{ p: 2 }}>
               <Stack direction={{ xs: 'column', md: 'row' }} justifyContent="space-between" spacing={2} sx={{ mb: 2 }}>
                <Typography variant="h2">Withdrawal History</Typography>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
                  <TextField size="small" label="From (DD/MM/YYYY)" type="date" value={from} onChange={(e) => setFrom(e.target.value)} InputLabelProps={{ shrink: true }} />
                  <TextField size="small" label="To (DD/MM/YYYY)" type="date" value={to} onChange={(e) => setTo(e.target.value)} InputLabelProps={{ shrink: true }} />
                  <Button
                    variant="outlined"
                    startIcon={<Download size={18} />}
                    href={statementUrl(investorId, from, to)}
                  >
                    CSV
                  </Button>
                </Stack>
              </Stack>
              <TableContainer>
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <TableCell>Date</TableCell>
                      <TableCell>Product</TableCell>
                      <TableCell align="right">Amount</TableCell>
                      <TableCell align="right">Before</TableCell>
                      <TableCell align="right">After</TableCell>
                      <TableCell>Status</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {withdrawals.map((withdrawal) => (
                      <TableRow key={withdrawal.id}>
                        <TableCell>{new Date(withdrawal.createdAt).toLocaleDateString('en-ZA')}</TableCell>
                        <TableCell>{withdrawal.productName}</TableCell>
                        <TableCell align="right" className="money">{money(withdrawal.amount)}</TableCell>
                        <TableCell align="right" className="money">{money(withdrawal.balanceBefore)}</TableCell>
                        <TableCell align="right" className="money">{money(withdrawal.balanceAfter)}</TableCell>
                        <TableCell><Chip size="small" color="success" label={withdrawal.status} /></TableCell>
                      </TableRow>
                    ))}
                    {withdrawals.length === 0 && (
                      <TableRow>
                        <TableCell colSpan={6} align="center">No withdrawals yet.</TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </TableContainer>
            </Paper>
          </Grid>
        </Grid>
      </Container>

      <Snackbar open={Boolean(notice)} autoHideDuration={5000} onClose={() => setNotice(null)}>
        <Alert severity={notice?.type ?? 'success'} variant="filled" onClose={() => setNotice(null)}>
          {notice?.text}
        </Alert>
      </Snackbar>
    </Box>
  );
}
