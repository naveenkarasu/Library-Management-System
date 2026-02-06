import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  BookOpen,
  Users,
  BookPlus,
  Undo2,
  FileBarChart,
  Library,
  X,
} from 'lucide-react'
import { cn } from '@/lib/utils'

interface SidebarProps {
  open: boolean;
  onClose: () => void;
}

const navigation = [
  { name: 'Dashboard', href: '/', icon: LayoutDashboard },
  { name: 'Books', href: '/books', icon: BookOpen },
  { name: 'Members', href: '/members', icon: Users },
  { name: 'Issue Book', href: '/issue', icon: BookPlus },
  { name: 'Return Book', href: '/return', icon: Undo2 },
  { name: 'Reports', href: '/reports', icon: FileBarChart },
]

export function Sidebar({ open, onClose }: SidebarProps) {
  return (
    <>
      {/* Mobile overlay */}
      {open && (
        <div
          className="fixed inset-0 z-40 bg-black/50 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <aside
        className={cn(
          "fixed inset-y-0 left-0 z-50 w-64 bg-teal-900 text-white transform transition-transform duration-200 ease-in-out lg:translate-x-0 lg:static lg:z-auto",
          open ? "translate-x-0" : "-translate-x-full"
        )}
      >
        {/* Logo */}
        <div className="flex items-center justify-between h-16 px-6 border-b border-teal-700">
          <div className="flex items-center gap-2">
            <Library className="h-6 w-6 text-gold-400" />
            <span className="text-lg font-bold">LibraryMS</span>
          </div>
          <button
            className="lg:hidden text-teal-300 hover:text-white"
            onClick={onClose}
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Navigation */}
        <nav className="mt-6 px-3">
          <ul className="space-y-1">
            {navigation.map((item) => (
              <li key={item.name}>
                <NavLink
                  to={item.href}
                  onClick={onClose}
                  className={({ isActive }) =>
                    cn(
                      "flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors",
                      isActive
                        ? "bg-teal-600 text-white border-l-2 border-gold-400"
                        : "text-teal-200 hover:bg-teal-800 hover:text-white"
                    )
                  }
                >
                  <item.icon className="h-5 w-5 flex-shrink-0" />
                  {item.name}
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>

        {/* Demo mode indicator */}
        {import.meta.env.VITE_DEMO_MODE === 'true' && (
          <div className="absolute bottom-4 left-3 right-3">
            <div className="rounded-lg bg-gold-500/20 border border-gold-500/30 px-3 py-2 text-xs text-gold-300">
              Demo Mode - Login: admin/admin
            </div>
          </div>
        )}
      </aside>
    </>
  )
}
