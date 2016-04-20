package com.metawiring.gen.core;

/**
 * Pair a targetContext C with a resolvable bindings template, allowing for production
 * of a bindable target object R with values applied for each call to @{link bind}.
 * The context object can be passed directly with the two-args constructor, or it can be realized
 * along with the the generator instances by overriding @{link resolveTargetContext}.
 *
 *
 * @param <C> The user context object. This is generally a template of some kind.
 * @param <R> The target of binding generated values to an object provided by the context object.
 */
public abstract class ResolvableContextBindings<C,R> {
    private GeneratorBindingsTemplate<C> bindingsTemplate;
    private GenBindings<C> genBindings;
    private C targetContext;

    /**
     * Create a resolvable bindings template to be paired with a target usr object.
     * If you use this constructor signature, override @link{resolveTargetContext} as well.
     * @param bindingsTemplate the resolvable bindings template
     */
    public ResolvableContextBindings(GeneratorBindingsTemplate<C> bindingsTemplate) {
        this.bindingsTemplate = bindingsTemplate;
    }

    public ResolvableContextBindings(GeneratorBindingsTemplate<C> bindingsTemplate, C targetContext) {
        this(bindingsTemplate);
        this.targetContext = targetContext;
    }

    public ResolvableContextBindings(GeneratorBindingsTemplate<C> bindingsTemplate, C targetContext, boolean resolveImmediately) {
        this(bindingsTemplate,targetContext);
        if (resolveImmediately)
            resolveBindings();
    }

    /**
     * Resolve and instantiate the associated generators and target object. This should be called from the
     * same scope from which it will be used.
     * The context object is returned
     */
    public GenBindings<C> resolveBindings() {
        targetContext = resolveTargetContext();
        this.genBindings = bindingsTemplate.resolveBindings();
        return genBindings;
    }

    public C getTargetContext() {
        return targetContext;
    }

    /**
     * Bind the generated values to a result object type R and return it.
     * @param value the input to all generator instances
     * @return target object R
     */
    public R bind(long value) {
        Object[] all = genBindings.getAll(value);
        return bindValues(all, targetContext);
    }

    /**
     * If the user object to be targeted by generated values is provided in type T, then this method
     * may be removed. However, if the object is to be resolved at the same time as generator instances,
     * then override this method to
     */
    private C resolveTargetContext() {
        return targetContext;
    }

    /**
     * Bind generated values to target object R.
     * @param values generated values
     * @param userBindingTarget the user context object which can provide an R instance
     * @return the bound target object, with values assigned
     */
    protected abstract R bindValues(Object[] values, C userBindingTarget);

}
