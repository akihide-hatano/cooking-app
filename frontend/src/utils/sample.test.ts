import { describe, expect, it } from "vitest";
import { add } from "./sample.ts";

describe("add", () => {
  it("2と3を渡すと5を返す", () => {
    expect(add(1, 2)).toBe(3);
  });
});
