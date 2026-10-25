import type { ButtonHTMLAttributes } from "react";

type ActionButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  fullWidth?: boolean;
};

export default function ActionButton({
  children,
  className = "",
  type = "button",
  fullWidth = true,
  ...props
}: ActionButtonProps) {
  return (
    <button
      {...props}
      type={type}
      className={`
        flex h-13 items-center justify-center
        rounded-button bg-action px-6
        font-bold text-white
        transition-colors hover:bg-action-dark
        disabled:cursor-not-allowed disabled:opacity-50
        ${fullWidth ? "w-full" : "w-auto"}
        ${className}
      `}
    >
      {children}
    </button>
  );
}
