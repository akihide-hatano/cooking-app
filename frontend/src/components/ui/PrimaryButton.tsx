import type { ButtonHTMLAttributes } from "react";

type PrimaryButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  fullWidth?: boolean;
};

export default function PrimaryButton({
  children,
  className = "",
  type = "button",
  fullWidth = true,
  ...props
}: PrimaryButtonProps) {
  return (
    <button
      type={type}
      className={`
        flex h-13 items-center justify-center
        rounded-button bg-brand px-6
        font-bold text-white shadow-sm
        transition-colors hover:bg-brand-dark
        disabled:cursor-not-allowed disabled:opacity-50
        ${fullWidth ? "w-full" : "w-full lg:w-auto"}
        ${className}
      `}
      {...props}
    >
      {children}
    </button>
  );
}
