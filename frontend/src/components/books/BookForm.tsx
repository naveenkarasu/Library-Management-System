import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog'
import type { Book, BookFormData } from '@/types'

interface BookFormProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: BookFormData) => void;
  book?: Book | null;
  isLoading?: boolean;
}

const defaultFormData: BookFormData = {
  title: '',
  author: '',
  isbn: '',
  publisher: '',
  year: new Date().getFullYear(),
  genre: '',
  totalCopies: 1,
  availableCopies: 1,
}

export function BookForm({ open, onClose, onSubmit, book, isLoading }: BookFormProps) {
  const [formData, setFormData] = useState<BookFormData>(defaultFormData)
  const [errors, setErrors] = useState<Partial<Record<keyof BookFormData, string>>>({})

  useEffect(() => {
    if (book) {
      setFormData({
        title: book.title,
        author: book.author,
        isbn: book.isbn,
        publisher: book.publisher,
        year: book.year,
        genre: book.genre,
        totalCopies: book.totalCopies,
        availableCopies: book.availableCopies,
      })
    } else {
      setFormData(defaultFormData)
    }
    setErrors({})
  }, [book, open])

  const validate = (): boolean => {
    const newErrors: Partial<Record<keyof BookFormData, string>> = {}
    if (!formData.title.trim()) newErrors.title = 'Title is required'
    if (!formData.author.trim()) newErrors.author = 'Author is required'
    if (!formData.isbn.trim()) newErrors.isbn = 'ISBN is required'
    if (formData.year < 1000 || formData.year > 2100) newErrors.year = 'Invalid year'
    if (formData.totalCopies < 1) newErrors.totalCopies = 'Must have at least 1 copy'
    if (formData.availableCopies < 0) newErrors.availableCopies = 'Cannot be negative'
    if (formData.availableCopies > formData.totalCopies) {
      newErrors.availableCopies = 'Cannot exceed total copies'
    }
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (validate()) {
      onSubmit(formData)
    }
  }

  const updateField = (field: keyof BookFormData, value: string | number) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: undefined }))
    }
  }

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent onClose={onClose} className="max-w-md max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{book ? 'Edit Book' : 'Add New Book'}</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="title">Title *</Label>
            <Input
              id="title"
              value={formData.title}
              onChange={(e) => updateField('title', e.target.value)}
              className={errors.title ? 'border-red-500' : ''}
            />
            {errors.title && <p className="text-xs text-red-500">{errors.title}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="author">Author *</Label>
            <Input
              id="author"
              value={formData.author}
              onChange={(e) => updateField('author', e.target.value)}
              className={errors.author ? 'border-red-500' : ''}
            />
            {errors.author && <p className="text-xs text-red-500">{errors.author}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="isbn">ISBN *</Label>
            <Input
              id="isbn"
              value={formData.isbn}
              onChange={(e) => updateField('isbn', e.target.value)}
              className={errors.isbn ? 'border-red-500' : ''}
            />
            {errors.isbn && <p className="text-xs text-red-500">{errors.isbn}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="publisher">Publisher</Label>
            <Input
              id="publisher"
              value={formData.publisher}
              onChange={(e) => updateField('publisher', e.target.value)}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="year">Year</Label>
              <Input
                id="year"
                type="number"
                value={formData.year}
                onChange={(e) => updateField('year', parseInt(e.target.value) || 0)}
                className={errors.year ? 'border-red-500' : ''}
              />
              {errors.year && <p className="text-xs text-red-500">{errors.year}</p>}
            </div>

            <div className="space-y-2">
              <Label htmlFor="genre">Genre</Label>
              <Input
                id="genre"
                value={formData.genre}
                onChange={(e) => updateField('genre', e.target.value)}
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="totalCopies">Total Copies</Label>
              <Input
                id="totalCopies"
                type="number"
                min={1}
                value={formData.totalCopies}
                onChange={(e) => updateField('totalCopies', parseInt(e.target.value) || 0)}
                className={errors.totalCopies ? 'border-red-500' : ''}
              />
              {errors.totalCopies && <p className="text-xs text-red-500">{errors.totalCopies}</p>}
            </div>

            <div className="space-y-2">
              <Label htmlFor="availableCopies">Available Copies</Label>
              <Input
                id="availableCopies"
                type="number"
                min={0}
                value={formData.availableCopies}
                onChange={(e) => updateField('availableCopies', parseInt(e.target.value) || 0)}
                className={errors.availableCopies ? 'border-red-500' : ''}
              />
              {errors.availableCopies && <p className="text-xs text-red-500">{errors.availableCopies}</p>}
            </div>
          </div>

          <DialogFooter>
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button type="submit" disabled={isLoading}>
              {isLoading ? 'Saving...' : book ? 'Update Book' : 'Add Book'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}
