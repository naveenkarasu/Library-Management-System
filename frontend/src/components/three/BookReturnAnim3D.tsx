import { useRef } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import * as THREE from 'three'

function AnimatedBook() {
  const groupRef = useRef<THREE.Group>(null)
  const coverRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (groupRef.current) {
      groupRef.current.rotation.y = Math.sin(state.clock.elapsedTime * 0.5) * 0.3
      groupRef.current.position.y = Math.sin(state.clock.elapsedTime * 0.7) * 0.1
    }
    if (coverRef.current) {
      const openAngle = (Math.sin(state.clock.elapsedTime * 0.8) + 1) * 0.3
      coverRef.current.rotation.y = -openAngle
    }
  })

  return (
    <group ref={groupRef} position={[0, 0, 0]}>
      {/* Book spine/back */}
      <mesh position={[0, 0, 0]}>
        <boxGeometry args={[0.8, 1.1, 0.12]} />
        <meshStandardMaterial color="#0d9488" roughness={0.4} metalness={0.3} />
      </mesh>
      {/* Book pages */}
      <mesh position={[0, 0, 0.07]}>
        <boxGeometry args={[0.75, 1.05, 0.08]} />
        <meshStandardMaterial color="#fefce8" roughness={0.8} />
      </mesh>
      {/* Front cover that opens */}
      <mesh ref={coverRef} position={[-0.38, 0, 0.12]}>
        <boxGeometry args={[0.8, 1.1, 0.03]} />
        <meshStandardMaterial color="#134e4a" roughness={0.4} metalness={0.3} />
      </mesh>
      {/* Gold title stripe */}
      <mesh position={[0, 0.2, 0.14]}>
        <boxGeometry args={[0.5, 0.08, 0.01]} />
        <meshStandardMaterial color="#d97706" roughness={0.2} metalness={0.6} />
      </mesh>
    </group>
  )
}

function FloatingPage({ position, delay }: {
  position: [number, number, number]
  delay: number
}) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      const t = state.clock.elapsedTime + delay
      meshRef.current.position.y = position[1] + Math.sin(t * 0.6) * 0.3
      meshRef.current.rotation.x = Math.sin(t * 0.4) * 0.2
      meshRef.current.rotation.z = Math.sin(t * 0.3) * 0.15
    }
  })

  return (
    <mesh ref={meshRef} position={position}>
      <planeGeometry args={[0.3, 0.4]} />
      <meshStandardMaterial color="#fefce8" side={THREE.DoubleSide} transparent opacity={0.6} />
    </mesh>
  )
}

export function BookReturnAnim3D() {
  return (
    <div className="w-full h-48 rounded-xl overflow-hidden bg-gradient-to-r from-teal-50 to-gold-50 border border-teal-100">
      <Canvas camera={{ position: [0, 0.5, 3], fov: 35 }}>
        <ambientLight intensity={0.6} />
        <directionalLight position={[3, 5, 5]} intensity={0.7} />
        <pointLight position={[-2, 2, 1]} intensity={0.3} color="#0d9488" />
        <pointLight position={[2, 1, 1]} intensity={0.2} color="#d97706" />
        <AnimatedBook />
        <FloatingPage position={[-0.8, 0.4, 0.5]} delay={0} />
        <FloatingPage position={[0.9, 0.2, 0.3]} delay={1.5} />
        <FloatingPage position={[0.5, 0.6, -0.2]} delay={3} />
      </Canvas>
    </div>
  )
}
