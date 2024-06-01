import type { PolymorphicProps } from "@kobalte/core";
import * as TextFieldPrimitive from "@kobalte/core/text-field";
import { type ValidComponent, splitProps } from "solid-js";
import { cn } from "~/lib/utils";

export const Root = <T extends ValidComponent = "div">(
    props: PolymorphicProps<T, TextFieldPrimitive.TextFieldRootProps<T>>,
) => {
    const [className, rest] = splitProps(
        props as PolymorphicProps<"div", TextFieldPrimitive.TextFieldRootProps<"div">>,
        ["class"],
    );

    return <TextFieldPrimitive.Root class={cn("flex flex-col gap-1", className)} {...rest} />;
};

export const Label = TextFieldPrimitive.Label;
export const ErrorMessage = TextFieldPrimitive.ErrorMessage;
export const Description = TextFieldPrimitive.Description;

export const Input = <T extends ValidComponent = "input">(
    props: PolymorphicProps<T, TextFieldPrimitive.TextFieldInputProps<T>>,
) => {
    const [className, rest] = splitProps(
        props as PolymorphicProps<"label", TextFieldPrimitive.TextFieldRootProps<"label">>,
        ["class"],
    );
    return <TextFieldPrimitive.Input class={cn("border rounded", className)} {...rest} />;
};

export const TextField = Object.assign(Root, { Label, Input, Description, ErrorMessage });
