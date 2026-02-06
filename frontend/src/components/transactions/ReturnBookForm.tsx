import { Button } from '@/components/ui/button'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { Badge } from '@/components/ui/badge'
import { formatDate } from '@/lib/utils'
import type { Transaction } from '@/types'

interface ReturnBookFormProps {
  transactions: Transaction[];
  onReturn: (transactionId: number) => void;
  isLoading?: boolean;
  returningId?: number | null;
}

export function ReturnBookForm({ transactions, onReturn, isLoading, returningId }: ReturnBookFormProps) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Book</TableHead>
          <TableHead className="hidden sm:table-cell">Member</TableHead>
          <TableHead className="hidden md:table-cell">Issue Date</TableHead>
          <TableHead>Due Date</TableHead>
          <TableHead>Status</TableHead>
          <TableHead className="hidden sm:table-cell">Fine</TableHead>
          <TableHead className="text-right">Action</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {transactions.length === 0 ? (
          <TableRow>
            <TableCell colSpan={7} className="text-center py-8 text-muted-foreground">
              No active transactions
            </TableCell>
          </TableRow>
        ) : (
          transactions.map((t) => (
            <TableRow key={t.id}>
              <TableCell className="font-medium">{t.bookTitle}</TableCell>
              <TableCell className="hidden sm:table-cell">{t.memberName}</TableCell>
              <TableCell className="hidden md:table-cell text-xs">
                {formatDate(t.issueDate)}
              </TableCell>
              <TableCell className="text-xs">{formatDate(t.dueDate)}</TableCell>
              <TableCell>
                {t.status === 'OVERDUE' ? (
                  <Badge variant="destructive">Overdue</Badge>
                ) : (
                  <Badge variant="default">Issued</Badge>
                )}
              </TableCell>
              <TableCell className="hidden sm:table-cell">
                {t.fine > 0 ? `$${t.fine.toFixed(2)}` : '-'}
              </TableCell>
              <TableCell className="text-right">
                <Button
                  size="sm"
                  onClick={() => onReturn(t.id)}
                  disabled={isLoading && returningId === t.id}
                >
                  {isLoading && returningId === t.id ? 'Returning...' : 'Return'}
                </Button>
              </TableCell>
            </TableRow>
          ))
        )}
      </TableBody>
    </Table>
  )
}
