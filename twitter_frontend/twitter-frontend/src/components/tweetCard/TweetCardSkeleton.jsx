import { Skeleton } from "@/components/ui/skeleton"

const TweetCardSkeleton = () => {
  return (
    <article className="border-b px-4 py-4">
      <div className="flex gap-3">
        <Skeleton className="h-10 w-10 flex-shrink-0 rounded-full" />

        <div className="min-w-0 flex-1">
          <div className="flex items-center gap-2">
            <Skeleton className="h-4 w-28" />
            <Skeleton className="h-3 w-20" />
            <Skeleton className="h-3 w-24" />
          </div>

          <div className="mt-2 space-y-2">
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-11/12" />
            <Skeleton className="h-4 w-2/3" />
          </div>

          <div className="mt-4 flex w-full items-center justify-around">
            <Skeleton className="h-4 w-10" />
            <Skeleton className="h-4 w-10" />
            <Skeleton className="h-4 w-10" />
            <Skeleton className="h-4 w-4 rounded-full" />
          </div>
        </div>
      </div>
    </article>
  )
}

export default TweetCardSkeleton
