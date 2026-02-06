import { useState, useMemo } from 'react'
import { useBooks, useCreateBook, useUpdateBook, useDeleteBook } from '@/hooks/useBooks'
import { BookTable } from '@/components/books/BookTable'
import { BookForm } from '@/components/books/BookForm'
import { BookSearch } from '@/components/books/BookSearch'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Spinner } from '@/components/ui/spinner'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog'
import { useToast } from '@/components/ui/toast'
import { Plus } from 'lucide-react'
import type { Book, BookFormData } from '@/types'

export function BooksPage() {
  const { data: books, isLoading } = useBooks()
  const createBook = useCreateBook()
  const updateBook = useUpdateBook()
  const deleteBook = useDeleteBook()
  const { addToast } = useToast()

  const [search, setSearch] = useState('')
  const [formOpen, setFormOpen] = useState(false)
  const [editingBook, setEditingBook] = useState<Book | null>(null)
  const [deleteConfirm, setDeleteConfirm] = useState<Book | null>(null)

  const filteredBooks = useMemo(() => {
    if (!books) return []
    if (!search.trim()) return books
    const query = search.toLowerCase()
    return books.filter(
      (book) =>
        book.title.toLowerCase().includes(query) ||
        book.author.toLowerCase().includes(query) ||
        book.isbn.toLowerCase().includes(query) ||
        book.genre.toLowerCase().includes(query)
    )
  }, [books, search])

  const handleAdd = () => {
    setEditingBook(null)
    setFormOpen(true)
  }

  const handleEdit = (book: Book) => {
    setEditingBook(book)
    setFormOpen(true)
  }

  const handleFormSubmit = (data: BookFormData) => {
    if (editingBook) {
      updateBook.mutate(
        { id: editingBook.id, data },
        {
          onSuccess: () => {
            setFormOpen(false)
            setEditingBook(null)
            addToast('Book updated successfully', 'success')
          },
          onError: () => {
            addToast('Failed to update book', 'error')
          },
        }
      )
    } else {
      createBook.mutate(data, {
        onSuccess: () => {
          setFormOpen(false)
          addToast('Book added successfully', 'success')
        },
        onError: () => {
          addToast('Failed to add book', 'error')
        },
      })
    }
  }

  const handleDelete = (book: Book) => {
    setDeleteConfirm(book)
  }

  const confirmDelete = () => {
    if (!deleteConfirm) return
    deleteBook.mutate(deleteConfirm.id, {
      onSuccess: () => {
        setDeleteConfirm(null)
        addToast('Book deleted successfully', 'success')
      },
      onError: () => {
        addToast('Failed to delete book', 'error')
      },
    })
  }

  if (isLoading) {
    return <Spinner className="py-20" />
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-stone-900">Books</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Manage the library book catalog
          </p>
        </div>
        <Button onClick={handleAdd}>
          <Plus className="h-4 w-4 mr-2" />
          Add Book
        </Button>
      </div>

      <Card>
        <CardHeader>
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <CardTitle>Book Catalog</CardTitle>
            <div className="w-full sm:w-72">
              <BookSearch value={search} onChange={setSearch} />
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <BookTable
            books={filteredBooks}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        </CardContent>
      </Card>

      <BookForm
        open={formOpen}
        onClose={() => {
          setFormOpen(false)
          setEditingBook(null)
        }}
        onSubmit={handleFormSubmit}
        book={editingBook}
        isLoading={createBook.isPending || updateBook.isPending}
      />

      <Dialog open={!!deleteConfirm} onOpenChange={() => setDeleteConfirm(null)}>
        <DialogContent onClose={() => setDeleteConfirm(null)} className="max-w-sm">
          <DialogHeader>
            <DialogTitle>Delete Book</DialogTitle>
          </DialogHeader>
          <p className="text-sm text-muted-foreground">
            Are you sure you want to delete &quot;{deleteConfirm?.title}&quot;? This action cannot be undone.
          </p>
          <DialogFooter>
            <Button variant="outline" onClick={() => setDeleteConfirm(null)}>
              Cancel
            </Button>
            <Button
              variant="destructive"
              onClick={confirmDelete}
              disabled={deleteBook.isPending}
            >
              {deleteBook.isPending ? 'Deleting...' : 'Delete'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}
