import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import * as THREE from 'three'

const memberColors: Record<string, string> = {
  FACULTY: '#0d9488',
  STAFF: '#d97706',
  STUDENT: '#16a34a',
}

function Node({ position, color, speed }: {
  position: [number, number, number]
  color: string
  speed: number
}) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.position.y = position[1] + Math.sin(state.clock.elapsedTime * speed + position[0]) * 0.15
    }
  })

  return (
    <mesh ref={meshRef} position={position}>
      <sphereGeometry args={[0.12, 16, 16]} />
      <meshStandardMaterial color={color} roughness={0.3} metalness={0.5} />
    </mesh>
  )
}

function Connection({ from, to, color }: {
  from: [number, number, number]
  to: [number, number, number]
  color: string
}) {
  const ref = useRef<THREE.Line>(null)

  const geometry = useMemo(() => {
    const points = [
      new THREE.Vector3(...from),
      new THREE.Vector3(...to),
    ]
    return new THREE.BufferGeometry().setFromPoints(points)
  }, [from, to])

  const material = useMemo(() => new THREE.LineBasicMaterial({ color, transparent: true, opacity: 0.3 }), [color])

  return <primitive ref={ref} object={new THREE.Line(geometry, material)} />
}

function Scene() {
  const groupRef = useRef<THREE.Group>(null)

  useFrame((state) => {
    if (groupRef.current) {
      groupRef.current.rotation.y = state.clock.elapsedTime * 0.1
    }
  })

  const nodes = useMemo(() => {
    const types = ['FACULTY', 'STAFF', 'STUDENT']
    return Array.from({ length: 18 }, (_, i) => {
      const type = types[i % 3]
      const angle = (i / 18) * Math.PI * 2
      const radius = 1 + (i % 3) * 0.6
      return {
        position: [
          Math.cos(angle) * radius,
          (Math.random() - 0.5) * 1.2,
          Math.sin(angle) * radius,
        ] as [number, number, number],
        color: memberColors[type],
        speed: 0.5 + Math.random() * 0.5,
        type,
      }
    })
  }, [])

  const connections = useMemo(() => {
    const conns: Array<{ from: [number, number, number]; to: [number, number, number]; color: string }> = []
    for (let i = 0; i < nodes.length; i++) {
      const next = (i + 1) % nodes.length
      if (Math.random() > 0.3) {
        conns.push({
          from: nodes[i].position,
          to: nodes[next].position,
          color: nodes[i].color,
        })
      }
    }
    return conns
  }, [nodes])

  return (
    <group ref={groupRef}>
      {nodes.map((node, i) => (
        <Node key={i} {...node} />
      ))}
      {connections.map((conn, i) => (
        <Connection key={i} {...conn} />
      ))}
    </group>
  )
}

export function MemberNetwork3D() {
  return (
    <div className="w-full h-48 rounded-xl overflow-hidden bg-gradient-to-r from-teal-50 to-gold-50 border border-teal-100">
      <Canvas camera={{ position: [0, 1, 3.5], fov: 40 }}>
        <ambientLight intensity={0.6} />
        <directionalLight position={[3, 5, 3]} intensity={0.7} />
        <pointLight position={[-2, 2, 1]} intensity={0.3} color="#0d9488" />
        <pointLight position={[2, -1, 1]} intensity={0.2} color="#d97706" />
        <Scene />
      </Canvas>
      <div className="flex justify-center gap-4 -mt-6 relative z-10 text-xs pb-2">
        <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-teal-600" /> Faculty</span>
        <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-gold-600" /> Staff</span>
        <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-green-600" /> Student</span>
      </div>
    </div>
  )
}
